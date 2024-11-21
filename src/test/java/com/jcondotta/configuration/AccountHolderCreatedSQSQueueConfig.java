package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.sqs.queues.account-holder-created")
public record AccountHolderCreatedSQSQueueConfig(@NotBlank String queueURL) {

}