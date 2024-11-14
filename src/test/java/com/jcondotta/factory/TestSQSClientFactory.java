package com.jcondotta.factory;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Factory
public class TestSQSClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(TestSQSClientFactory.class);

    @Singleton
    @Replaces(SqsClient.class)
    @Requires(property = "aws.sqs.endpoint", pattern = "(.|\\s)*\\S(.|\\s)*")
    public SqsClient sqsClientEndpointOverridden(AwsCredentials awsCredentials, Region region, @Value("${aws.sqs.endpoint}") String endpoint){
        logger.info("Building SQSClient with params: awsCredentials: {}, region: {} and endpoint: {}", awsCredentials, region, endpoint);

        return SqsClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}