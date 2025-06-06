package com.jcondotta.container;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service;

@Testcontainers
public class LocalStackTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    Logger LOGGER = LoggerFactory.getLogger(LocalStackTestContainer.class);

    String LOCAL_STACK_IMAGE_NAME = "localstack/localstack:3.7.0";
    DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse(LOCAL_STACK_IMAGE_NAME);

    LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(Service.DYNAMODB, Service.SNS, Service.SQS)
            .withCopyFileToContainer(
                    MountableFile.forHostPath("localstack/init-aws.sh"),
                    "/etc/localstack/init/ready.d/init-aws.sh"
            )
            .withLogConsumer(outputFrame -> LOGGER.info(outputFrame.getUtf8StringWithoutLineEnding()));

    AtomicBoolean CONTAINER_STARTED = new AtomicBoolean(false);

    protected void startContainer() {
        try {
            Startables.deepStart(LOCALSTACK_CONTAINER).join();
        }
        catch (Exception e) {
            LOGGER.error("Failed to start LocalStack container: {}", e.getMessage());
            throw new RuntimeException("Failed to start LocalStack container", e);
        }
    }

    protected Map<String, String> getContainerProperties() {
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

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext){
        if (CONTAINER_STARTED.compareAndSet(false, true)) {
            startContainer();
            TestPropertyValues.of(getContainerProperties()).applyTo(applicationContext.getEnvironment());
        }
        else {
            LOGGER.warn("getProperties() called multiple times; container already started.");
        }
    }
}
