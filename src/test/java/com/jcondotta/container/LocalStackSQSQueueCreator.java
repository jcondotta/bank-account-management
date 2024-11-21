package com.jcondotta.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.util.Objects;

import static com.jcondotta.container.LocalStackTestContainer.LOCALSTACK_CONTAINER;

public class LocalStackSQSQueueCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalStackSQSQueueCreator.class);

    public static String createQueueWithURLResponse(SqsClient sqsClient, String queueName) {
        if (!LOCALSTACK_CONTAINER.isRunning()) {
            LOGGER.error("LocalStack is not running. Please ensure the container is started.");

            throw new IllegalStateException("LocalStack container is not running.");
        }

        return createQueue(sqsClient, queueName);
    }

    public static String createQueueWithURLResponse(String queueName) {
        var sqsClient = initializeSQSClient();
        return createQueueWithURLResponse(sqsClient, queueName);
    }

    private static String createQueue(SqsClient sqsClient, String queueName) {
        sqsClient = Objects.requireNonNullElseGet(sqsClient, LocalStackSQSQueueCreator::initializeSQSClient);

        var existentQueue = sqsClient.listQueues().queueUrls()
                .stream()
                .filter(queueUrl -> queueUrl.endsWith("/" + queueName))
                .findFirst();

        if (existentQueue.isPresent()) {
            var queueUrl = existentQueue.get();
            LOGGER.info("Queue '{}' already exists with URL: {}", queueName, queueUrl);
            return queueUrl;
        }

        var createdQueue = sqsClient.createQueue(builder -> builder.queueName(queueName).build());
        String createdQueueUrl = createdQueue.queueUrl();
        LOGGER.info("Queue '{}' created successfully with URL: {}", queueName, createdQueueUrl);

        return createdQueueUrl;
    }

    private static SqsClient initializeSQSClient() {
        var awsCredentials = AwsBasicCredentials.create(
                LOCALSTACK_CONTAINER.getAccessKey(),
                LOCALSTACK_CONTAINER.getSecretKey()
        );
        var region = Region.of(LOCALSTACK_CONTAINER.getRegion());
        var endpoint = LOCALSTACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.SQS).toString();

        return SqsClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
