package com.jcondotta.service.bank_account;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.EntityType;
import com.jcondotta.service.dto.BankAccountDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class FindBankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBankAccountService.class);

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Inject
    public FindBankAccountService(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable) {
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public Optional<BankAccountDTO> findBankAccountById(@NotNull UUID bankAccountId) {
        var partitionKey = BankingEntity.BANK_ACCOUNT_PK_TEMPLATE.formatted(bankAccountId.toString());
        var queryConditional = QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(partitionKey)
                .build());

        var bankingEntities = bankingEntityDynamoDbTable.query(queryConditional)
                .items().stream()
                .toList();

        var bankAccount = bankingEntities.stream()
                .filter(bankingEntity -> bankingEntity.getEntityType().equals(EntityType.BANK_ACCOUNT))
                .findFirst();

        var accountHolders = bankingEntities.stream()
                .filter(bankingEntity -> bankingEntity.getEntityType().equals(EntityType.ACCOUNT_HOLDER))
                .toList();

        return bankAccount
                .map(bankingEntity -> new BankAccountDTO(bankingEntity, accountHolders));

    }
}
