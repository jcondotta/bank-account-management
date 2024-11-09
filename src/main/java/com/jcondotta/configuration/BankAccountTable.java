package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.dynamodb.tables.bank-accounts")
public record BankAccountTable(@NotBlank String tableName) {

}