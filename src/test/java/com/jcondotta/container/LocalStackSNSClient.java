package com.jcondotta.container;

import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

import static com.jcondotta.container.LocalStackTestContainer.LOCALSTACK_CONTAINER;

public class LocalStackSNSClient {

    public static SnsClient initializeSNSClient() {
        var awsCredentials = AwsBasicCredentials.create(
                LOCALSTACK_CONTAINER.getAccessKey(),
                LOCALSTACK_CONTAINER.getSecretKey()
        );
        var region = Region.of(LOCALSTACK_CONTAINER.getRegion());
        var endpoint = LOCALSTACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.SQS).toString();

        return SnsClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
