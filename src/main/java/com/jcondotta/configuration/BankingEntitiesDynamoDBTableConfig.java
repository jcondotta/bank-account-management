package com.jcondotta.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "aws.dynamodb.tables.banking-entities")
public record BankingEntitiesDynamoDBTableConfig(@NotBlank String tableName) {}
