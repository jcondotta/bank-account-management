package com.jcondotta.factory.aws.dynamodb;

import com.jcondotta.configuration.BankAccountTable;
import com.jcondotta.domain.BankAccount;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Factory
public class DynamoDBTableFactory {

    @Singleton
    @Requires(bean = BankAccountTable.class)
    public DynamoDbTable<BankAccount> dynamoDbTable(DynamoDbEnhancedClient dynamoDbEnhancedClient, BankAccountTable bankAccountTable){
        StaticTableSchema<BankAccount> staticTableSchema = StaticTableSchema.builder(BankAccount.class)
                .newItemSupplier(BankAccount::new)
                .addAttribute(UUID.class, attr -> attr.name("bankAccountId")
                        .getter(BankAccount::getBankAccountId)
                        .setter(BankAccount::setBankAccountId)
                        .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, attr -> attr.name("accountHolderName")
                        .getter(BankAccount::getAccountHolderName)
                        .setter(BankAccount::setAccountHolderName))
                .addAttribute(String.class, attr -> attr.name("passportNumber")
                        .getter(BankAccount::getPassportNumber)
                        .setter(BankAccount::setPassportNumber))
                .addAttribute(LocalDate.class, attr -> attr.name("dateOfBirth")
                        .getter(BankAccount::getDateOfBirth)
                        .setter(BankAccount::setDateOfBirth))
                .addAttribute(String.class, attr -> attr.name("iban")
                        .getter(BankAccount::getIban)
                        .setter(BankAccount::setIban))
                .addAttribute(LocalDateTime.class, attr -> attr.name("dateOfOpening")
                        .getter(BankAccount::getDateOfOpening)
                        .setter(BankAccount::setDateOfOpening))
                .build();

        return dynamoDbEnhancedClient.table(bankAccountTable.tableName(), staticTableSchema);
    }
}