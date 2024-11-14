package com.jcondotta.event;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.Optional;

@Singleton
public class SNSTopicArnFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSTopicArnFinder.class);

    private final SnsClient snsClient;

    @Inject
    public SNSTopicArnFinder(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public Optional<String> findTopicARN(String topicName) {
        LOGGER.debug("Attempting to find ARN for SNS topic with name: {}", topicName);

        var topicARN = snsClient.listTopics().topics()
                .stream()
                .map(snsTopic -> snsTopic.topicArn())
                .filter(topicArn -> topicArn.endsWith(":" + topicName))
                .findFirst();

        if (topicARN.isPresent()) {
            LOGGER.info("Found SNS topic ARN for '{}': {}", topicName, topicARN.get());
        }
        else {
            LOGGER.warn("SNS topic with name '{}' not found", topicName);
        }

        return topicARN;
    }
}
