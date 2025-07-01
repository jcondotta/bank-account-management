package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.application.ports.output.repository.AccountHolderLookupRepository;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.infrastructure.adapters.persistence.entity.AccountHolderKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.AccountHolderEntityMapper;
import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.time.Clock;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccountHolderLookupDynamoDBRepository implements AccountHolderLookupRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderLookupDynamoDBRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final AccountHolderEntityMapper accountHolderEntityMapper;
    private final Clock clock;

    @Override
    public Optional<AccountHolder> lookup(@NotNull AccountHolderLookupRequest accountHolderLookupRequest) {
        var bankAccountId = accountHolderLookupRequest.bankAccountId();
        var accountHolderId = accountHolderLookupRequest.accountHolderId();

        var partitionKey = AccountHolderKey.partitionKey(bankAccountId);
        var sortKey = AccountHolderKey.sortKey(accountHolderId);

        var key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        LOGGER.atInfo()
            .setMessage("Querying account holder details for accountHolderId: {} and bankAccountId: {}")
            .addArgument(accountHolderId.toString())
            .addArgument(bankAccountId.toString())
            .log();

        var bankingEntity = bankingEntityDynamoDbTable.getItem(builder -> builder.key(key));
        return Optional.ofNullable(bankingEntity)
                .map(accountHolderEntity -> accountHolderEntityMapper.toDomain(accountHolderEntity, clock));
    }
}