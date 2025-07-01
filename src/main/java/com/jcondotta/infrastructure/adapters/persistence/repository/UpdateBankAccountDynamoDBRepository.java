package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.AccountHolderEntityMapper;
import com.jcondotta.infrastructure.adapters.persistence.mapper.BankAccountEntityMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

@Repository
@AllArgsConstructor
public class UpdateBankAccountDynamoDBRepository implements UpdateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateBankAccountDynamoDBRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final BankAccountEntityMapper mapper;
    private final AccountHolderEntityMapper accountHolderEntityMapper;

    @Override
    public void update(BankAccount bankAccount) {
        LOGGER.info("Updating bank account and holders for ID: {}", bankAccount.bankAccountId());

        var entities = mapper.toBankingEntities(bankAccount, accountHolderEntityMapper);

        var txBuilder = TransactWriteItemsEnhancedRequest.builder();

        entities.forEach(entity ->
            txBuilder.addPutItem(bankingEntityDynamoDbTable, entity)
        );

        dynamoDbEnhancedClient.transactWriteItems(txBuilder.build());

        LOGGER.debug("Successfully updated bank account {}", bankAccount.bankAccountId());
    }
}