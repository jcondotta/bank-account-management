package com.jcondotta.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.service.dto.AccountHolderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Component
public class AccountHolderCreatedSNSTopicPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderCreatedSNSTopicPublisher.class);

    private final SnsClient snsClient;
    private final AccountHolderCreatedSNSTopicConfig snsTopicConfig;
    private final ObjectMapper objectMapper;


    public AccountHolderCreatedSNSTopicPublisher(SnsClient snsClient, AccountHolderCreatedSNSTopicConfig snsTopicConfig, ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.snsTopicConfig = snsTopicConfig;
        this.objectMapper = objectMapper;
    }

    public String publishMessage(AccountHolderDTO accountHolderDTO) {
        var accountHolderCreatedNotification = new AccountHolderCreatedNotification(
                accountHolderDTO.getAccountHolderId(),
                accountHolderDTO.getAccountHolderName(),
                accountHolderDTO.getBankAccountId()
        );
        LOGGER.atInfo()
                .setMessage("Preparing notification message for Account Holder Created event.")
                .addKeyValue("accountHolderId", accountHolderDTO.getAccountHolderId().toString())
                .addKeyValue("accountHolderName", accountHolderDTO.getAccountHolderName())
                .addKeyValue("bankAccountId", accountHolderDTO.getBankAccountId().toString())
                .log();

        // Serializing the notification to JSON
        String notificationMessage = null;
        try {
            notificationMessage = objectMapper.writeValueAsString(accountHolderCreatedNotification);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        LOGGER.atInfo()
                .setMessage("Serialized notification message.")
                .addKeyValue("notificationMessage", notificationMessage)
                .log();


        // Creating the PublishRequest for SNS
        var publishRequest = PublishRequest.builder()
                .message(notificationMessage)
                .topicArn(snsTopicConfig.topicArn())
                .build();

        LOGGER.atInfo()
                .setMessage("Sending notification to SNS.")
                .addKeyValue("snsTopicArn", snsTopicConfig.topicArn())
                .log();

        // Publishing the message
        var publishResponse = snsClient.publish(publishRequest);

        LOGGER.atInfo()
                .setMessage("Notification successfully published to SNS.")
                .addKeyValue("snsTopicArn", snsTopicConfig.topicArn())
                .addKeyValue("messageId", publishResponse.messageId())
                .log();

        return publishResponse.messageId();

    }
}
