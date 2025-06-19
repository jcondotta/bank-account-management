package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.application.ports.output.repository.CreateJointAccountHolderRepository;
import com.jcondotta.domain.model.BankingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Objects;

@Repository
public class CreateJointAccountHolderDynamoDBRepository implements CreateJointAccountHolderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderDynamoDBRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    public CreateJointAccountHolderDynamoDBRepository(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable){
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public void createJointAccountHolder(BankingEntity jointAccountHolder) {
        Objects.requireNonNull(jointAccountHolder, "accountHolder.notNull");

        LOGGER.atInfo()
                .setMessage("Starting to create joint account holder in DB associated with bankAccountId: {}")
                .addArgument(jointAccountHolder.getBankAccountId())
                .addKeyValue("bankAccountId", jointAccountHolder.getBankAccountId().toString())
                .log();

        bankingEntityDynamoDbTable.putItem(jointAccountHolder);

        LOGGER.atDebug()
                .setMessage("Successfully saved joint account holder to DB.")
                .addKeyValue("bankAccountId", jointAccountHolder.getBankAccountId().toString())
                .addKeyValue("accountHolderId", jointAccountHolder.getAccountHolderId().toString())
                .log();
    }
}
