package com.jcondotta.config.aws.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Configuration
public class SNSClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSClientConfig.class);

    @Bean
    @ConditionalOnProperty(name = "aws.sns.endpoint")
    public SnsClient snsClientLocal(AwsCredentialsProvider awsCredentialsProvider,
                                    Region region,
                                    @Value("${aws.sns.endpoint}") String endpoint) {

        LOGGER.atInfo()
                .setMessage("Initializing SNSClient for custom endpoint: {}")
                .addArgument(endpoint)
                .log();

        return SnsClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }
}
