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

import java.util.List;
import java.util.Objects;

@Singleton
public class CreateJointAccountHoldersRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Inject
    public CreateJointAccountHoldersRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void create(List<BankingEntity> jointAccountHolders) {
        Objects.requireNonNull(jointAccountHolders, "accountHolders.notNull");

        if (jointAccountHolders.isEmpty()) {
            LOGGER.warn("No joint account holders provided. Skipping transaction.");
            throw new IllegalArgumentException("accountHolders.notEmpty");
        }

        try {
            var transactionWriteBuilder = TransactWriteItemsEnhancedRequest.builder();
            jointAccountHolders.stream()
                .forEach(jointAccountHolder -> {
                    try {
                        MDC.put("bankAccountId", jointAccountHolder.getBankAccountId().toString());
                        MDC.put("accountHolderId", jointAccountHolder.getAccountHolderId().toString());

                        LOGGER.info("Saving account holder to DB.");

                        transactionWriteBuilder.addPutItem(bankingEntityDynamoDbTable, jointAccountHolder);
                    }
                    finally {
                        MDC.popByKey("accountHolderId");
                    }
                }
            );

            dynamoDbEnhancedClient.transactWriteItems(transactionWriteBuilder.build());

            LOGGER.info("Successfully saved all joint account holders to the database.");
        }
        finally {
            MDC.clear();
        }
    }
}
