package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.application.ports.output.repository.AddJointAccountHolderRepository;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.AccountHolderEntityMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Repository
@AllArgsConstructor
public class AddJointAccountHolderDynamoDBRepository implements AddJointAccountHolderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddJointAccountHolderDynamoDBRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final AccountHolderEntityMapper mapper;

    public void save(BankAccountId bankAccountId, AccountHolder accountHolder) {
        LOGGER.atInfo()
                .setMessage("Starting to create joint account holder in DB associated with bankAccountId: {}")
                .addArgument(bankAccountId.value())
                .addKeyValue("bankAccountId", bankAccountId.toString())
                .log();

        bankingEntityDynamoDbTable.putItem(mapper.toEntity(bankAccountId, accountHolder));

        LOGGER.atDebug()
                .setMessage("Successfully saved joint account holder to DB.")
                .addKeyValue("bankAccountId", bankAccountId.toString())
                .addKeyValue("accountHolderId", accountHolder.accountHolderId().toString())
                .log();
    }
}
