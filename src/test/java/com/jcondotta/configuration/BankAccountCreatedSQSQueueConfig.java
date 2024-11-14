package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.sqs.queues.bank-account-created")
public record BankAccountCreatedSQSQueueConfig(@NotBlank String queueName) {

}