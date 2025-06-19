package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.application.ports.output.repository.AccountHolderLookupRepository;
import com.jcondotta.domain.model.BankingEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccountHolderLookupDynamoDBRepository implements AccountHolderLookupRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderLookupDynamoDBRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Override
    public Optional<BankingEntity> lookup(@NotNull AccountHolderLookupRequest accountHolderLookupRequest) {
        var bankAccountId = accountHolderLookupRequest.bankAccountId().toString();
        var accountHolderId = accountHolderLookupRequest.accountHolderId().toString();

        var partitionKey = BankingEntity.ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId);
        var sortKey = BankingEntity.ACCOUNT_HOLDER_SK_TEMPLATE.formatted(accountHolderId);

        var key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        LOGGER.atInfo()
            .setMessage("Querying account holder details for bankAccountId: {} and accountHolderId: {}")
            .addArgument(bankAccountId)
            .addArgument(accountHolderId)
            .log();

        return Optional.ofNullable(
            bankingEntityDynamoDbTable.getItem(builder -> builder.key(key))
        );
    }
}