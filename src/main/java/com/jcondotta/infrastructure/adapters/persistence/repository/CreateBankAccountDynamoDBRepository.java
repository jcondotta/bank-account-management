package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.BankingEntityAssemblerMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

@Repository
@AllArgsConstructor
public class CreateBankAccountDynamoDBRepository implements CreateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountDynamoDBRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final BankingEntityAssemblerMapper bankingEntityAssemblerMapper;

    @Override
    public void save(BankAccount bankAccount) {
        LOGGER.atInfo()
            .setMessage("Starting transaction to save bank account.")
            .log();

        TransactWriteItemsEnhancedRequest.Builder builder = TransactWriteItemsEnhancedRequest.builder();
        bankingEntityAssemblerMapper.toEntities(bankAccount)
            .forEach(bankingEntity -> builder
                .addPutItem(bankingEntityDynamoDbTable, bankingEntity)
            );

        dynamoDbEnhancedClient.transactWriteItems(builder.build());

        LOGGER.atInfo()
            .setMessage("Bank account and primary account holder created successfully.")
            .log();
    }
}