package com.jcondotta.web.controller;

import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.configuration.AccountHolderCreatedSQSQueueConfig;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.event.AccountHolderCreatedNotification;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateBankAccountRequest;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.http.HttpStatus;
import io.micronaut.json.JsonMapper;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.io.IOException;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class AccountHolderCreatedSNSTopicIT implements LocalStackTestContainer {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    @Inject
    JsonMapper jsonMapper;

    @Inject
    RequestSpecification requestSpecification;

    @Inject
    SnsClient snsClient;

    @Inject
    AccountHolderCreatedSNSTopicConfig snsTopicConfig;

    @Inject
    SqsClient sqsClient;

    @Inject
    AccountHolderCreatedSQSQueueConfig sqsQueueConfig;
    String bankAccountCreatedSQSQueueARN;

    @BeforeEach
    void beforeEach(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification
                .basePath(BankAccountURIBuilder.ACCOUNT_HOLDERS_API_V1_MAPPING)
                .contentType(ContentType.JSON);

        this.bankAccountCreatedSQSQueueARN = getQueueArn(sqsClient, sqsQueueConfig.queueURL());
        subscribeTopicWithQueue(snsClient, snsTopicConfig.topicArn(), bankAccountCreatedSQSQueueARN);
    }

    @Test
    void shouldPublishMessageSuccessfullyToSNSTopic_whenJointAccountHolderIsCreated() throws IOException {
        var bankAccountId = TestBankAccountId.ITALY.getBankAccountId();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var accountHolderDTO = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", bankAccountId)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.getCode())
                .extract()
                    .response()
                        .as(AccountHolderDTO.class);

        var messages = sqsClient.receiveMessage(builder -> builder.queueUrl(sqsQueueConfig.queueURL())
                        .waitTimeSeconds(3)
                        .build())
                .messages();

        assertThat(messages)
                .hasSize(1)
                .first()
                .satisfies(message -> {
                    var snsMessageWrapper = jsonMapper.readValue(message.body(), JsonNode.class);
                    var rawMessage = snsMessageWrapper.get("Message").getStringValue();

//                    var notification = jsonMapper.readValue(rawMessage, AccountHolderCreatedNotification.class);
//                    assertThat(notification.bankAccountId()).isEqualTo(accountHolderDTO.getBankAccountId());
//                    assertThat(notification.accountHolderId()).isEqualTo(accountHolderDTO.getAccountHolderId());
//                    assertThat(notification.accountHolderName()).isEqualTo(accountHolderDTO.getAccountHolderName());
            }
        );
    }

    private SubscribeResponse subscribeTopicWithQueue(SnsClient snsClient, String topicARN, String queueARN){
        return snsClient.subscribe(builder -> builder
                .topicArn(topicARN)
                .protocol("sqs")
                .endpoint(queueARN));
    }

    private String getQueueArn(SqsClient sqsClient, String queueUrl) {
        return sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder()
                        .queueUrl(queueUrl)
                        .attributeNames(QueueAttributeName.QUEUE_ARN)
                        .build())
                .attributes()
                .get(QueueAttributeName.QUEUE_ARN);
    }
}
