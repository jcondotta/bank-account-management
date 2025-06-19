package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.ports.output.repository.AccountHoldersQueryRepository;
import com.jcondotta.domain.model.BankingEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@AllArgsConstructor
@Repository(value = "accountHoldersDynamoDBRepository")
public class AccountHoldersQueryDynamoDBRepository implements AccountHoldersQueryRepository{

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHoldersQueryDynamoDBRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Override
    public List<BankingEntity> query(@NotNull BankAccountId bankAccountId) {
        var partitionKey = BankingEntity.ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId.toString());
        var sortKeyPrefix = BankingEntity.ACCOUNT_HOLDER_SK_TEMPLATE.formatted(StringUtils.EMPTY);

        var queryConditional = QueryConditional.sortBeginsWith(Key.builder()
            .partitionValue(partitionKey)
            .sortValue(sortKeyPrefix)
            .build());

        LOGGER.atInfo()
            .setMessage("Querying account holders details for bankAccountId: {}")
            .addArgument(bankAccountId)
            .log();

        return bankingEntityDynamoDbTable.query(queryConditional)
            .items()
            .stream()
            .toList();
    }
}