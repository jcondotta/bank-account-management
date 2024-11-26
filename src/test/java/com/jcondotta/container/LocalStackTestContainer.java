package com.jcondotta.container;

import io.micronaut.test.support.TestPropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Testcontainers
public interface LocalStackTestContainer extends TestPropertyProvider {

    Logger LOGGER = LoggerFactory.getLogger(LocalStackTestContainer.class);

    String LOCAL_STACK_IMAGE_NAME = "localstack/localstack:3.7.0";
    DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse(LOCAL_STACK_IMAGE_NAME);

    LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(Service.DYNAMODB, Service.SNS, Service.SQS)
            .withLogConsumer(outputFrame -> LOGGER.debug(outputFrame.getUtf8StringWithoutLineEnding()));

    AtomicBoolean CONTAINER_STARTED = new AtomicBoolean(false);

    String SNS_ACCOUNT_HOLDER_CREATED_TOPIC_NAME = "account-holder-created-topic-test";
    String SNS_ACCOUNT_HOLDER_CREATED_QUEUE_NAME = "account-holder-created-queue-test";

    Map<String, String> CONTAINER_PROPERTIES = new HashMap<>();

    @Override
    default Map<String, String> getProperties() {
        if (CONTAINER_STARTED.compareAndSet(false, true)) {
            startContainer();

            var snsTopicARN = LocalStackSNSTopicCreator.createTopicWithARNResponse(SNS_ACCOUNT_HOLDER_CREATED_TOPIC_NAME);
            var sqsQueueURL = LocalStackSQSQueueCreator.createQueueWithURLResponse(SNS_ACCOUNT_HOLDER_CREATED_QUEUE_NAME);

            CONTAINER_PROPERTIES.putAll(getContainerProperties());
            CONTAINER_PROPERTIES.put("AWS_SNS_ACCOUNT_HOLDER_CREATED_TOPIC_ARN", snsTopicARN);
            CONTAINER_PROPERTIES.put("AWS_SQS_ACCOUNT_HOLDER_CREATED_QUEUE_URL", sqsQueueURL);

            logContainerConfiguration(CONTAINER_PROPERTIES);
        }
        else {
            LOGGER.warn("getProperties() called multiple times; container already started.");
        }

        return CONTAINER_PROPERTIES;
    }

    default void startContainer() {
        try {
            Startables.deepStart(LOCALSTACK_CONTAINER).join();
        }
        catch (Exception e) {
            LOGGER.error("Failed to start LocalStack container: {}", e.getMessage());
            throw new RuntimeException("Failed to start LocalStack container", e);
        }
    }

    default Map<String, String> getContainerProperties() {
        var mapContainerProperties = Map.ofEntries(
                Map.entry("AWS_ACCESS_KEY_ID", LOCALSTACK_CONTAINER.getAccessKey()),
                Map.entry("AWS_SECRET_ACCESS_KEY", LOCALSTACK_CONTAINER.getSecretKey()),
                Map.entry("AWS_DEFAULT_REGION", LOCALSTACK_CONTAINER.getRegion()),
                Map.entry("AWS_DYNAMODB_ENDPOINT", LOCALSTACK_CONTAINER.getEndpointOverride(Service.DYNAMODB).toString()),
                Map.entry("AWS_SNS_ENDPOINT", LOCALSTACK_CONTAINER.getEndpointOverride(Service.SNS).toString()),
                Map.entry("AWS_SQS_ENDPOINT", LOCALSTACK_CONTAINER.getEndpointOverride(Service.SQS).toString())
        );

        return new HashMap<>(mapContainerProperties);
    }

    default void logContainerConfiguration(Map<String, String> containerProperties) {
        var logBuilder = new StringBuilder();
        logBuilder.append("\n================== Container Configuration ==================\n");

        containerProperties.forEach((key, value) ->
                logBuilder.append(String.format("  %s : %s%n", key, value))
        );

        logBuilder.append("=============================================================\n");

        LOGGER.info(logBuilder.toString());
    }
}
