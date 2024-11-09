package com.jcondotta.container;

import io.micronaut.test.support.TestPropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@Testcontainers
public interface LocalStackTestContainer extends TestPropertyProvider {

    Logger LOGGER = LoggerFactory.getLogger(LocalStackTestContainer.class);

    String LOCAL_STACK_IMAGE_NAME = "localstack/localstack:3.7.0";
    DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse(LOCAL_STACK_IMAGE_NAME);

    LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(Service.DYNAMODB)
            .withLogConsumer(outputFrame -> LOGGER.debug(outputFrame.getUtf8StringWithoutLineEnding()));

    @Override
    default Map<String, String> getProperties() {
        try {
            Startables.deepStart(LOCALSTACK_CONTAINER).join();
        }
        catch (Exception e) {
            LOGGER.error("Failed to start LocalStack container: {}", e.getMessage());

            throw new RuntimeException("Failed to start LocalStack container", e);
        }

        logContainerConfiguration();

        return getContainerProperties();
    }

    default Map<String, String> getContainerProperties() {
        return Map.ofEntries(
                Map.entry("AWS_ACCESS_KEY_ID", LOCALSTACK_CONTAINER.getAccessKey()),
                Map.entry("AWS_SECRET_ACCESS_KEY", LOCALSTACK_CONTAINER.getSecretKey()),
                Map.entry("AWS_DEFAULT_REGION", LOCALSTACK_CONTAINER.getRegion()),
                Map.entry("AWS_DYNAMODB_ENDPOINT", LOCALSTACK_CONTAINER.getEndpointOverride(Service.DYNAMODB).toString())
        );
    }

    default void logContainerConfiguration() {
        LOGGER.info("\n================== Container Configuration ==================\n" +
                "LocalStack Configuration:\n" +
                String.format("  Access Key       : %s%n", LOCALSTACK_CONTAINER.getAccessKey()) +
                String.format("  Secret Key       : %s%n", LOCALSTACK_CONTAINER.getSecretKey()) +
                String.format("  Region           : %s%n", LOCALSTACK_CONTAINER.getRegion()) +
                String.format("  DynamoDB Endpoint: %s%n", LOCALSTACK_CONTAINER.getEndpointOverride(Service.DYNAMODB)) +
                "===========================================================\n");
    }
}