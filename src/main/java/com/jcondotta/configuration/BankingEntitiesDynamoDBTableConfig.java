package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.dynamodb.tables.banking-entities")
public record BankingEntitiesDynamoDBTableConfig(@NotBlank String tableName) {

}