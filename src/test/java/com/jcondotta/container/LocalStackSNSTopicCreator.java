package com.jcondotta.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;

import java.util.Objects;

import static com.jcondotta.container.LocalStackTestContainer.LOCALSTACK_CONTAINER;

public class LocalStackSNSTopicCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalStackSNSTopicCreator.class);

    public static String createTopicWithARNResponse(String topicName) {
        if (!LOCALSTACK_CONTAINER.isRunning()) {
            LOGGER.error("LocalStack is not running. Please ensure the container is started.");

            throw new IllegalStateException("LocalStack container is not running.");
        }

        var snsClient = LocalStackSNSClient.initializeSNSClient();
        return createTopic(snsClient, topicName);
    }

    private static String createTopic(SnsClient snsClient, String topicName) {
        snsClient = Objects.requireNonNullElseGet(snsClient, LocalStackSNSClient::initializeSNSClient);

        var existentTopic = snsClient.listTopics().topics()
                .stream()
                .map(snsTopic -> snsTopic.topicArn())
                .filter(topicArn -> topicArn.endsWith(":" + topicName))
                .findFirst();

        if(existentTopic.isPresent()){
            var topicARN = existentTopic.get();
            LOGGER.info("Topic '{}' already exists with ARN: {}", topicName, topicARN);
            return topicARN;
        }

        CreateTopicResponse createTopicResponse = snsClient.createTopic(builder -> builder.name(topicName).build());
        LOGGER.info("Topic '{}' created successfully with ARN: {}", topicName, createTopicResponse.topicArn());

        return createTopicResponse.topicArn();
    }
}
