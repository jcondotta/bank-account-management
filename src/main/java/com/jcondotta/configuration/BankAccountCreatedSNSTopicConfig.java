package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.sns.topics.bank-account-created")
public record BankAccountCreatedSNSTopicConfig(@NotBlank String topicName) {

}