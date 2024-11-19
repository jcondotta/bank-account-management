package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.util.List;
import java.util.Objects;

@Singleton
public class CreateJointAccountHolderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Inject
    public CreateJointAccountHolderRepository(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void create(List<BankingEntity> jointAccountHolders) {
        Objects.requireNonNull(jointAccountHolders, "accountHolders.notNull");

        jointAccountHolders.forEach(jointAccountHolder -> bankingEntityDynamoDbTable.putItem(jointAccountHolder));
    }

    public void create(BankingEntity jointAccountHolder) {
        create(List.of(jointAccountHolder));
//        Objects.requireNonNull(jointAccountHolder, "accountHolder.notNull");
//
//        try {
//            MDC.put("bankAccountId", jointAccountHolder.getBankAccountId().toString());
//            MDC.put("accountHolderId", jointAccountHolder.getAccountHolderId().toString());
//
//            LOGGER.debug("Initiating transaction to add a joint account holder.");
//
//            bankingEntityDynamoDbTable.putItem(jointAccountHolder);
//
//            LOGGER.info("Successfully saved joint account holder.");
//        }
//        finally {
//            MDC.clear();
//        }
    }
}
