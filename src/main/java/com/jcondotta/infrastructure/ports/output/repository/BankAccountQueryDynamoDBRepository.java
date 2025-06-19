package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.ports.output.repository.BankAccountQueryRepository;
import com.jcondotta.domain.model.BankingEntity;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Repository(value = "bankingEntitiesDynamoDBRepository")
public class BankAccountQueryDynamoDBRepository implements BankAccountQueryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountQueryDynamoDBRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    public BankAccountQueryDynamoDBRepository(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable) {
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    @Override
    public List<BankingEntity> query(@NotNull BankAccountId bankAccountId) {
        var partitionKey = BankingEntity.BANK_ACCOUNT_PK_TEMPLATE.formatted(bankAccountId.toString());
        var queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(partitionKey)
                .build());

        LOGGER.atInfo()
            .setMessage("Querying bank account and account holders details for bankAccountId: {}")
            .addArgument(bankAccountId)
            .log();

        return bankingEntityDynamoDbTable.query(queryConditional)
                .items().stream()
                .toList();
    }
}