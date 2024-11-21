package com.jcondotta.event;

import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.factory.TestAccountHolderFactory;
import com.jcondotta.factory.TestBankAccountFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.dto.BankAccountDTO;
import io.micronaut.json.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountCreatedSNSTopicPublisherTest {

    private static final String BANK_ACCOUNT_CREATED_TOPIC_ARN = "arn:aws:sns:us-east-1:123456789012:bank-account-created-topic";

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
    private static final String MESSAGE_ID = "message-id-12345";

    @Mock
    private SnsClient snsClient;

    @Mock
    private AccountHolderCreatedSNSTopicConfig snsTopicConfig;

    private BankAccountCreatedSNSTopicPublisher snsTopicPublisher;

    private BankAccountDTO bankAccountDTO;
    private JsonMapper jsonMapper = JsonMapper.createDefault();

    private PublishResponse publishResponse = PublishResponse.builder().messageId(MESSAGE_ID).build();

    @BeforeEach
    void beforeEach() {
        snsTopicPublisher = new BankAccountCreatedSNSTopicPublisher(snsClient, snsTopicConfig, jsonMapper);
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

    @Test
    void shouldPublishMessage_whenPrimaryAccountHolderExistsInBankAccountDTO() throws IOException {
        when(snsTopicConfig.topicArn()).thenReturn(BANK_ACCOUNT_CREATED_TOPIC_ARN);
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(publishResponse);

        var bankAccount = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);
        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );

        bankAccountDTO = new BankAccountDTO(bankAccount, primaryAccountHolder);

        var returnedMessageId = snsTopicPublisher.publishMessage(bankAccountDTO);
        assertThat(returnedMessageId).isEqualTo(MESSAGE_ID);

        var publishRequestCaptor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient).publish(publishRequestCaptor.capture());

        var capturedRequest = publishRequestCaptor.getValue();
        assertThat(capturedRequest.topicArn()).isEqualTo(BANK_ACCOUNT_CREATED_TOPIC_ARN);

        var expectedNotification = new BankAccountCreatedNotification(
                BANK_ACCOUNT_ID_BRAZIL,
                primaryAccountHolder.getAccountHolderId(),
                primaryAccountHolder.getAccountHolderName()
        );
        assertThat(capturedRequest.message()).isEqualTo(jsonMapper.writeValueAsString(expectedNotification));

        verify(snsTopicConfig, times(2)).topicArn();
    }

    @Test
    void shouldThrowIllegalStateException_whenNoPrimaryAccountHolderExistsInBankAccountDTO(){
        var bankAccount = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);
        var jointAccountHolder = TestAccountHolderFactory.createJointAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );

        bankAccountDTO = new BankAccountDTO(bankAccount, jointAccountHolder);
        var exception = assertThrows(IllegalStateException.class, () -> snsTopicPublisher.publishMessage(bankAccountDTO));

        assertThat(exception)
                .hasMessage("bankAccount.primaryAccountHolder.notFound");
    }

    @Test
    void shouldThrowIllegalStateException_whenNoAccountHolderExistsInBankAccountDTO(){
        var bankAccount = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);

        bankAccountDTO = new BankAccountDTO(bankAccount, List.of());
        var exception = assertThrows(IllegalStateException.class, () -> snsTopicPublisher.publishMessage(bankAccountDTO));

        assertThat(exception)
                .hasMessage("bankAccount.primaryAccountHolder.notFound");
    }
}
