package com.jcondotta.event;

import com.jcondotta.configuration.BankAccountCreatedSNSTopicConfig;
import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.exception.NotificationSerializationException;
import com.jcondotta.service.dto.BankAccountDTO;
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
public class BankAccountCreatedSNSTopicPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountCreatedSNSTopicPublisher.class);

    private final SnsClient snsClient;
    private final BankAccountCreatedSNSTopicConfig snsTopicConfig;
    private final JsonMapper jsonMapper;

    @Inject
    public BankAccountCreatedSNSTopicPublisher(SnsClient snsClient, BankAccountCreatedSNSTopicConfig snsTopicConfig, JsonMapper jsonMapper) {
        this.snsClient = snsClient;
        this.snsTopicConfig = snsTopicConfig;
        this.jsonMapper = jsonMapper;
    }

    public String publishMessage(BankAccountDTO bankAccountDTO) {
        try {
            var bankAccountCreatedNotification = bankAccountDTO.getAccountHolders().stream()
                .filter(accountHolderDTO -> AccountHolderType.PRIMARY.equals(accountHolderDTO.getAccountHolderType()))
                .findFirst()
                .map(accountHolderDTO -> new BankAccountCreatedNotification(
                        bankAccountDTO.getBankAccountId(),
                        accountHolderDTO.getAccountHolderId(),
                        accountHolderDTO.getAccountHolderName())
                )
                .orElseThrow(() -> {
                            LOGGER.error("Primary account holder not found for BankAccount ID: {}", bankAccountDTO.getBankAccountId());
                            return new IllegalStateException("bankAccount.primaryAccountHolder.notFound");
                        }
                );

            MDC.put("bankAccountId", bankAccountCreatedNotification.bankAccountId().toString());
            MDC.put("accountHolderId", bankAccountCreatedNotification.accountHolderId().toString());

            var notification = serializeNotification(bankAccountCreatedNotification);

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

    private String serializeNotification(BankAccountCreatedNotification notification) {
        try {
            var serializedNotification = jsonMapper.writeValueAsString(notification);
            LOGGER.debug("Successfully serialized notification: {}", serializedNotification);

            return serializedNotification;
        }
        catch (IOException e) {
            LOGGER.error("Failed to serialize notification: {}", notification, e);
            throw new NotificationSerializationException("Error serializing notification", e);
        }
    }
}
