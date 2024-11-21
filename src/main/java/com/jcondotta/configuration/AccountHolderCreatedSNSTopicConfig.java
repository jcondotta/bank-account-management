package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.sns.topics.account-holder-created")
public record AccountHolderCreatedSNSTopicConfig(@NotBlank String topicArn) {

}