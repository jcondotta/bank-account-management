package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.application.ports.output.repository.LookupBankAccountRepository;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountEntityKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.BankAccountEntityMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class LookupDynamoDBBankAccountRepository implements LookupBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupDynamoDBBankAccountRepository.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final BankAccountEntityMapper bankAccountEntityMapper;

    @Override
    public Optional<BankAccount> lookup(BankAccountId bankAccountId) {
        var partitionKey = BankAccountEntityKey.partitionKey(bankAccountId);
        var queryConditional = QueryConditional.keyEqualTo(Key.builder()
            .partitionValue(partitionKey)
            .build());

        LOGGER.atInfo()
            .setMessage("Querying bank account and account holders for bankAccountId: {}")
            .addArgument(bankAccountId)
            .log();

        var bankingEntities = bankingEntityDynamoDbTable.query(queryConditional)
            .items().stream()
            .toList();

        return Optional.ofNullable(findBankAccountEntity(bankingEntities))
            .map(bankAccountEntity -> {
                var accountHolderEntities = findAccountHolderEntities(bankingEntities);
                return bankAccountEntityMapper.toDomain(bankAccountEntity, accountHolderEntities);
            });
    }

    private BankingEntity findBankAccountEntity(List<BankingEntity> items) {
        return items.stream()
            .filter(BankingEntity::isEntityTypeBankAccount)
            .findFirst()
            .orElse(null);
    }

    private List<BankingEntity> findAccountHolderEntities(List<BankingEntity> items) {
        return items.stream()
            .filter(BankingEntity::isEntityTypeAccountHolder)
            .toList();
    }
}