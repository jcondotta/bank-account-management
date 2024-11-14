package com.jcondotta.factory.aws.sns;

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
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Factory
public class SNSClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSClientFactory.class);

    @Singleton
    @Replaces(SnsClient.class)
    @Requires(missingProperty = "aws.sns.endpoint")
    public SnsClient snsClient(Region region){
        var environmentVariableCredentialsProvider = EnvironmentVariableCredentialsProvider.create();
        var awsCredentials = environmentVariableCredentialsProvider.resolveCredentials();

        LOGGER.info("Building SNSClient with params: awsCredentials: {} and region: {}", awsCredentials, region);

        return SnsClient.builder()
                .region(region)
                .credentialsProvider(environmentVariableCredentialsProvider)
                .build();
    }

    @Singleton
    @Replaces(SnsClient.class)
    @Requires(property = "aws.sns.endpoint")
    public SnsClient snsClientEndpointOverridden(AwsCredentials awsCredentials, Region region, @Value("${aws.sns.endpoint}") String snsEndpoint){
        LOGGER.info("Building SNSClient with params: awsCredentials: {}, region: {} and endpoint: {}", awsCredentials, region, snsEndpoint);

        return SnsClient.builder()
                .region(region)
                .endpointOverride(URI.create(snsEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}