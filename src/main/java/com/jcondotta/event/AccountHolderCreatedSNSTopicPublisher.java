package com.jcondotta.event;

import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.service.SerializationService;
import com.jcondotta.service.dto.AccountHolderDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Singleton
public class AccountHolderCreatedSNSTopicPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderCreatedSNSTopicPublisher.class);

    private final SnsClient snsClient;
    private final AccountHolderCreatedSNSTopicConfig snsTopicConfig;
    private final SerializationService serializationService;

    @Inject
    public AccountHolderCreatedSNSTopicPublisher(SnsClient snsClient, AccountHolderCreatedSNSTopicConfig snsTopicConfig, SerializationService serializationService) {
        this.snsClient = snsClient;
        this.snsTopicConfig = snsTopicConfig;
        this.serializationService = serializationService;
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

            var notification = serializationService.serialize(accountHolderCreatedNotification);

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
}
