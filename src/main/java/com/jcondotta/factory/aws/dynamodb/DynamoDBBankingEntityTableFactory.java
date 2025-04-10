package com.jcondotta.factory.aws.dynamodb;

import com.jcondotta.configuration.BankingEntitiesDynamoDBTableConfig;
import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.EntityType;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Factory
public class DynamoDBBankingEntityTableFactory {

    @Singleton
    public DynamoDbTable<BankingEntity> bankingEntityTable(DynamoDbEnhancedClient dynamoDbEnhancedClient, BankingEntitiesDynamoDBTableConfig bankingEntitiesDynamoDBTableConfig) {
        StaticTableSchema<BankingEntity> bankingEntitySchema = StaticTableSchema.builder(BankingEntity.class)
                .newItemSupplier(BankingEntity::new)
                .addAttribute(String.class, a -> a.name("partitionKey")
                        .getter(BankingEntity::getPartitionKey)
                        .setter(BankingEntity::setPartitionKey)
                        .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, a -> a.name("sortKey")
                        .getter(BankingEntity::getSortKey)
                        .setter(BankingEntity::setSortKey)
                        .tags(StaticAttributeTags.primarySortKey()))
                .addAttribute(EntityType.class, a -> a.name("entityType")
                        .getter(BankingEntity::getEntityType)
                        .setter(BankingEntity::setEntityType))

                // BankAccount Attributes
                .addAttribute(UUID.class, a -> a.name("bankAccountId")
                        .getter(BankingEntity::getBankAccountId)
                        .setter(BankingEntity::setBankAccountId))
                .addAttribute(String.class, a -> a.name("iban")
                        .getter(BankingEntity::getIban)
                        .setter(BankingEntity::setIban)
                        .tags(StaticAttributeTags.secondaryPartitionKey("bank-account-iban-gsi")))

                // AccountHolder Attributes
                .addAttribute(UUID.class, a -> a.name("accountHolderId")
                        .getter(BankingEntity::getAccountHolderId)
                        .setter(BankingEntity::setAccountHolderId))
                .addAttribute(String.class, a -> a.name("accountHolderName")
                        .getter(BankingEntity::getAccountHolderName)
                        .setter(BankingEntity::setAccountHolderName))
                .addAttribute(String.class, a -> a.name("passportNumber")
                        .getter(BankingEntity::getPassportNumber)
                        .setter(BankingEntity::setPassportNumber))
                .addAttribute(LocalDate.class, a -> a.name("dateOfBirth")
                        .getter(BankingEntity::getDateOfBirth)
                        .setter(BankingEntity::setDateOfBirth))
                .addAttribute(AccountHolderType.class, a -> a.name("accountHolderType")
                        .getter(BankingEntity::getAccountHolderType)
                        .setter(BankingEntity::setAccountHolderType))

                .addAttribute(LocalDateTime.class, a -> a.name("createdAt")
                        .getter(BankingEntity::getCreatedAt)
                        .setter(BankingEntity::setCreatedAt))
            .build();

        return dynamoDbEnhancedClient.table(bankingEntitiesDynamoDBTableConfig.tableName(), bankingEntitySchema);
    }
}
