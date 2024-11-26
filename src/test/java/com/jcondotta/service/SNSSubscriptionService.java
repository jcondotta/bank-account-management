package com.jcondotta.service;

import jakarta.inject.Singleton;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Singleton
public class SNSSubscriptionService {

    private final SnsClient snsClient;

    public SNSSubscriptionService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public SubscribeResponse subscribeQueueToTopic(String topicArn, String queueArn) {
        try {
            return snsClient.subscribe(builder -> builder
                    .topicArn(topicArn)
                    .protocol("sqs")
                    .endpoint(queueArn));
        }
        catch (SnsException e) {
            throw new RuntimeException("Failed to subscribe SQS queue to SNS topic", e);
        }
    }
}
