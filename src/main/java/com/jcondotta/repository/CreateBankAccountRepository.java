package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.Objects;

@Repository
public class CreateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    public CreateBankAccountRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void create(BankingEntity bankAccount, BankingEntity accountHolder) {
        Objects.requireNonNull(bankAccount, "bankAccount.notNull");
        Objects.requireNonNull(accountHolder, "accountHolder.notNull");

        LOGGER.atInfo()
                .setMessage("Initiating transaction to create bank account and account holder.")
                .addKeyValue("bankAccountId", bankAccount.getBankAccountId().toString())
                .addKeyValue("accountHolderId", accountHolder.getAccountHolderId().toString())
                .log();

        var transactWriteRequest = TransactWriteItemsEnhancedRequest.builder()
                .addPutItem(bankingEntityDynamoDbTable, bankAccount)
                .addPutItem(bankingEntityDynamoDbTable, accountHolder)
                .build();

        dynamoDbEnhancedClient.transactWriteItems(transactWriteRequest);

        LOGGER.atInfo()
                .setMessage("Initiating transaction to create bank account and account holder.")
                .addKeyValue("bankAccountId", bankAccount.getBankAccountId().toString())
                .addKeyValue("accountHolderId", accountHolder.getAccountHolderId().toString())
                .log();
    }
}
