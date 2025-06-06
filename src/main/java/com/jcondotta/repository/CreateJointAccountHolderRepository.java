package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Objects;

@Repository
public class CreateJointAccountHolderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    public CreateJointAccountHolderRepository(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void create(BankingEntity jointAccountHolder) {
        Objects.requireNonNull(jointAccountHolder, "accountHolder.notNull");

        bankingEntityDynamoDbTable.putItem(jointAccountHolder);

        LOGGER.atInfo()
                .setMessage("Successfully saved joint account holder to DB.")
                .addKeyValue("bankAccountId", jointAccountHolder.getBankAccountId().toString())
                .addKeyValue("accountHolderId", jointAccountHolder.getAccountHolderId().toString())
                .log();
    }
}
