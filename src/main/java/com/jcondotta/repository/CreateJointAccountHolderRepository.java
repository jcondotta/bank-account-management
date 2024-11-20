package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Objects;

@Singleton
public class CreateJointAccountHolderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Inject
    public CreateJointAccountHolderRepository(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void create(BankingEntity jointAccountHolder) {
        Objects.requireNonNull(jointAccountHolder, "accountHolder.notNull");

        try {
            MDC.put("bankAccountId", jointAccountHolder.getBankAccountId().toString());
            MDC.put("accountHolderId", jointAccountHolder.getAccountHolderId().toString());

            bankingEntityDynamoDbTable.putItem(jointAccountHolder);

            LOGGER.info("Successfully saved joint account holder to DB.");
        }
        finally {
            MDC.clear();
        }
    }
}
