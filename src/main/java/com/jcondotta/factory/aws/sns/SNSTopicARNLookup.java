package com.jcondotta.factory.aws.sns;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.Optional;

@Singleton
public class SNSTopicARNLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSTopicARNLookup.class);

    private final SnsClient snsClient;

    @Inject
    public SNSTopicARNLookup(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public Optional<String> getTopicARN(String topicName) {
        LOGGER.info("Ensuring SNS topic exists with name: {}", topicName);

        return snsClient.listTopics().topics()
                .stream()
                .map(snsTopic -> snsTopic.topicArn())
                .filter(topicArn -> topicArn.endsWith(":" + topicName))
                .findFirst();
    }
}
