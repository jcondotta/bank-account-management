package com.jcondotta.factory.aws.dynamodb;

import com.jcondotta.configuration.BankingEntitiesTable;
import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.AccountHolderType;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

import java.time.LocalDate;
import java.util.UUID;

@Factory
public class DynamoDBAccountHolderTableFactory {

    @Singleton
    @Requires(bean = BankingEntitiesTable.class)
    public DynamoDbTable<AccountHolder> accountHolderTable(DynamoDbEnhancedClient dynamoDbEnhancedClient, BankingEntitiesTable bankingEntitiesTable) {
        StaticTableSchema<AccountHolder> accountHolderSchema = StaticTableSchema.builder(AccountHolder.class)
                .newItemSupplier(AccountHolder::new)
                .addAttribute(String.class, attr -> attr.name("PK")
                        .getter(AccountHolder::getPk)
                        .setter(AccountHolder::setPk)
                        .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, attr -> attr.name("SK")
                        .getter(AccountHolder::getSk)
                        .setter(AccountHolder::setSk)
                        .tags(StaticAttributeTags.primarySortKey()))
                .addAttribute(String.class, attr -> attr.name("entityType")
                        .getter(AccountHolder::getEntityType)
                        .setter(AccountHolder::setEntityType))
                .addAttribute(UUID.class, a -> a.name("bankAccountId")
                        .getter(AccountHolder::getBankAccountId)
                        .setter(AccountHolder::setBankAccountId))
                .addAttribute(UUID.class, attr -> attr.name("accountHolderId")
                        .getter(AccountHolder::getAccountHolderId)
                        .setter(AccountHolder::setAccountHolderId))
                .addAttribute(String.class, attr -> attr.name("accountHolderName")
                        .getter(AccountHolder::getAccountHolderName)
                        .setter(AccountHolder::setAccountHolderName))
                .addAttribute(String.class, attr -> attr.name("passportNumber")
                        .getter(AccountHolder::getPassportNumber)
                        .setter(AccountHolder::setPassportNumber))
                .addAttribute(LocalDate.class, attr -> attr.name("dateOfBirth")
                        .getter(AccountHolder::getDateOfBirth)
                        .setter(AccountHolder::setDateOfBirth))
                .addAttribute(AccountHolderType.class, attr -> attr.name("accountHolderType")
                        .getter(AccountHolder::getAccountHolderType)
                        .setter(AccountHolder::setAccountHolderType))
                .build();

        return dynamoDbEnhancedClient.table(bankingEntitiesTable.tableName(), accountHolderSchema);
    }
}
