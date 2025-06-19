package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.domain.model.BankingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.List;
import java.util.Objects;

@Repository
public class CreateBankAccountDynamoDBRepository implements CreateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountDynamoDBRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    public CreateBankAccountDynamoDBRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void createBankAccount(BankingEntity bankAccount, BankingEntity accountHolder) {
        Objects.requireNonNull(accountHolder, "accountHolder.notNull");
        createBankAccount(bankAccount, List.of(accountHolder));
    }

    @Override
    public void createBankAccount(BankingEntity bankAccount, List<BankingEntity> accountHolders) {
        Objects.requireNonNull(bankAccount, "bankAccount.notNull");
        Objects.requireNonNull(accountHolders, "accountHolders.notNull");

        LOGGER.atInfo()
                .setMessage("Initiating transaction to create bank account and account holder(s).")
                .addKeyValue("bankAccountId", bankAccount.getBankAccountId().toString())
                .log();

        TransactWriteItemsEnhancedRequest.Builder builder = TransactWriteItemsEnhancedRequest.builder()
                .addPutItem(bankingEntityDynamoDbTable, bankAccount);

        accountHolders.forEach(accountHolder ->
                builder.addPutItem(bankingEntityDynamoDbTable, accountHolder));

        var transactWriteRequest = builder.build();

        dynamoDbEnhancedClient.transactWriteItems(transactWriteRequest);

        LOGGER.atInfo()
                .setMessage("Bank account and account holder(s) created successfully")
                .addKeyValue("bankAccountId", bankAccount.getBankAccountId().toString())
                .log();
    }
}