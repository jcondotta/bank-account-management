package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.Objects;

@Singleton
public class CreateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Inject
    public CreateBankAccountRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void create(BankingEntity bankAccount, BankingEntity accountHolder) {
        Objects.requireNonNull(bankAccount, "bankAccount.notNull");
        Objects.requireNonNull(accountHolder, "accountHolder.notNull");

        try {
            MDC.put("bankAccountId", bankAccount.getBankAccountId().toString());
            MDC.put("accountHolderId", accountHolder.getAccountHolderId().toString());

            LOGGER.debug("Initiating transaction to create bank account and account holder.");

            var transactWriteRequest = TransactWriteItemsEnhancedRequest.builder()
                    .addPutItem(bankingEntityDynamoDbTable, bankAccount)
                    .addPutItem(bankingEntityDynamoDbTable, accountHolder)
                    .build();

            dynamoDbEnhancedClient.transactWriteItems(transactWriteRequest);

            LOGGER.info("Successfully saved bank account and primary account holder to DB.");
        }
        finally {
            MDC.clear();
        }
    }
}
