package com.jcondotta.event;

import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.factory.TestAccountHolderFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.dto.AccountHolderDTO;
import io.micronaut.json.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountHolderCreatedSNSTopicPublisherTest {

    private static final String BANK_ACCOUNT_CREATED_TOPIC_ARN = "arn:aws:sns:us-east-1:123456789012:bank-account-created-topic";

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
    private static final String MESSAGE_ID = "message-id-12345";

    @Mock
    private SnsClient snsClient;

    @Mock
    private AccountHolderCreatedSNSTopicConfig snsTopicConfig;

    private AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;

    private JsonMapper jsonMapper = JsonMapper.createDefault();

    private PublishResponse publishResponse = PublishResponse.builder().messageId(MESSAGE_ID).build();

    @BeforeEach
    void beforeEach() {
        snsTopicPublisher = new AccountHolderCreatedSNSTopicPublisher(snsClient, snsTopicConfig, jsonMapper);
    }

    @AfterEach
    void afterEach(){
        assertThat(MDC.get("bankAccountId"))
                .as("MDC should be cleared after the publishMessage method completes for bankAccountId")
                .isNull();
        assertThat(MDC.get("accountHolderId"))
                .as("MDC should be cleared after the publishMessage method completes for accountHolderId")
                .isNull();
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldPublishMessage_whenAccountHolderIsValid(AccountHolderType accountHolderType) throws IOException {
        when(snsTopicConfig.topicArn()).thenReturn(BANK_ACCOUNT_CREATED_TOPIC_ARN);
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(publishResponse);

        var accountHolder = TestAccountHolderFactory.create(
                TestAccountHolderRequest.JEFFERSON,
                accountHolderType,
                BANK_ACCOUNT_ID_BRAZIL
        );

        var accountHolderDTO = new AccountHolderDTO(accountHolder);
        var returnedMessageId = snsTopicPublisher.publishMessage(accountHolderDTO);
        assertThat(returnedMessageId).isEqualTo(MESSAGE_ID);

        var publishRequestCaptor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient).publish(publishRequestCaptor.capture());

        var capturedRequest = publishRequestCaptor.getValue();
        assertThat(capturedRequest.topicArn()).isEqualTo(BANK_ACCOUNT_CREATED_TOPIC_ARN);

        var expectedNotification = new AccountHolderCreatedNotification(
                accountHolder.getAccountHolderId(),
                accountHolder.getAccountHolderName(),
                BANK_ACCOUNT_ID_BRAZIL
        );
        assertThat(capturedRequest.message()).isEqualTo(jsonMapper.writeValueAsString(expectedNotification));

        verify(snsTopicConfig, times(2)).topicArn();
    }
}
