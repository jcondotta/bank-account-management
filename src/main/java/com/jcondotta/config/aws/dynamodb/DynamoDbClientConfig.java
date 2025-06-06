package com.jcondotta.config.aws.dynamodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDbClientConfig.class);

    @Bean
    @ConditionalOnProperty(name = "aws.dynamodb.endpoint")
    public DynamoDbClient dynamoDbClientLocal(AwsCredentialsProvider awsCredentialsProvider,
                                              Region region,
                                              @Value("${aws.dynamodb.endpoint}") String endpoint) {

        LOGGER.atInfo()
                .setMessage("Initializing DynamoDbClient for custom endpoint: {}")
                .addArgument(endpoint)
                .log();

        return DynamoDbClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
