package com.jcondotta.factory.aws.dynamodb;

import com.jcondotta.configuration.BankingEntitiesTable;
import com.jcondotta.domain.BankAccount;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

import java.time.LocalDateTime;
import java.util.UUID;

@Factory
public class DynamoDBBankAccountTableFactory {

    @Singleton
    @Requires(bean = BankingEntitiesTable.class)
    public DynamoDbTable<BankAccount> bankAccountTable(DynamoDbEnhancedClient dynamoDbEnhancedClient, BankingEntitiesTable bankingEntitiesTable) {
        StaticTableSchema<BankAccount> bankAccountSchema = StaticTableSchema.builder(BankAccount.class)
                .newItemSupplier(BankAccount::new)
                .addAttribute(String.class, attr -> attr.name("PK")
                        .getter(BankAccount::getPk)
                        .setter(BankAccount::setPk)
                        .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, attr -> attr.name("SK")
                        .getter(BankAccount::getSk)
                        .setter(BankAccount::setSk)
                        .tags(StaticAttributeTags.primarySortKey()))
                .addAttribute(String.class, attr -> attr.name("entityType")
                        .getter(BankAccount::getEntityType)
                        .setter(BankAccount::setEntityType))
                .addAttribute(UUID.class, attr -> attr.name("bankAccountId")
                        .getter(BankAccount::getBankAccountId)
                        .setter(BankAccount::setBankAccountId))
                .addAttribute(String.class, attr -> attr.name("iban")
                        .getter(BankAccount::getIban)
                        .setter(BankAccount::setIban))
                .addAttribute(LocalDateTime.class, attr -> attr.name("dateOfOpening")
                        .getter(BankAccount::getDateOfOpening)
                        .setter(BankAccount::setDateOfOpening))
                .build();

        return dynamoDbEnhancedClient.table(bankingEntitiesTable.tableName(), bankAccountSchema);
    }
}