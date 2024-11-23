package com.jcondotta.event;

import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.service.dto.AccountHolderDTO;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.io.IOException;

@Singleton
public class AccountHolderCreatedSNSTopicPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderCreatedSNSTopicPublisher.class);

    private final SnsClient snsClient;
    private final AccountHolderCreatedSNSTopicConfig snsTopicConfig;
    private final JsonMapper jsonMapper;

    @Inject
    public AccountHolderCreatedSNSTopicPublisher(SnsClient snsClient, AccountHolderCreatedSNSTopicConfig snsTopicConfig, JsonMapper jsonMapper) {
        this.snsClient = snsClient;
        this.snsTopicConfig = snsTopicConfig;
        this.jsonMapper = jsonMapper;
    }

    public String publishMessage(AccountHolderDTO accountHolderDTO) {
        try {
            var accountHolderCreatedNotification = new AccountHolderCreatedNotification(
                    accountHolderDTO.getAccountHolderId(),
                    accountHolderDTO.getAccountHolderName(),
                    accountHolderDTO.getBankAccountId()
            );

            MDC.put("bankAccountId", accountHolderCreatedNotification.bankAccountId().toString());
            MDC.put("accountHolderId", accountHolderCreatedNotification.accountHolderId().toString());

            var notification = serializeNotification(accountHolderCreatedNotification);


            var publishRequest = PublishRequest.builder()
                    .message(notification)
                    .topicArn(snsTopicConfig.topicArn())
                    .build();

            var publishResponse = snsClient.publish(publishRequest);

            LOGGER.info("Successfully published message to SNS topic '{}'. Message ID: {}", snsTopicConfig.topicArn(), publishResponse.messageId());

            return publishResponse.messageId();
        }
        finally {
            MDC.clear();
        }
    }

    private String serializeNotification(AccountHolderCreatedNotification notification) {
        try {
            var serializedNotification = jsonMapper.writeValueAsString(notification);
            LOGGER.debug("Successfully serialized notification: {}", serializedNotification);

            return serializedNotification;
        }
        catch (IOException e) {
            LOGGER.error("Failed to serialize notification: {}", notification, e);

            throw new RuntimeException("Error serializing notification", e);
        }
    }
}
