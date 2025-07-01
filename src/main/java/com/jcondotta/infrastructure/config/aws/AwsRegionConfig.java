package com.jcondotta.infrastructure.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsRegionConfig {

    @Bean
    @ConditionalOnProperty(name = "aws.region")
    public Region awsRegion(@Value("${aws.region}") String region) {
        return Region.of(region);
    }
}
