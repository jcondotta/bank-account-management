package com.jcondotta.service.bank_account;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.EntityType;
import com.jcondotta.exception.BankAccountNotFoundException;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.UUID;

@Singleton
public class FindBankAccountService {

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Inject
    public FindBankAccountService(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable) {
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
    }

    public BankAccountDTO findBankAccountById(@NotNull UUID bankAccountId) {
        var partitionKey = BankingEntity.BANK_ACCOUNT_PK_TEMPLATE.formatted(bankAccountId.toString());
        var queryConditional = QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(partitionKey)
                .build());

        var bankingEntities = bankingEntityDynamoDbTable.query(queryConditional)
                .items().stream()
                .toList();

        var bankAccount = bankingEntities.stream()
                .filter(bankingEntity -> bankingEntity.getEntityType().equals(EntityType.BANK_ACCOUNT))
                .findFirst()
                .orElseThrow(() -> new BankAccountNotFoundException("bankAccount.notFound", bankAccountId));

        var accountHolders = bankingEntities.stream()
                .filter(bankingEntity -> bankingEntity.getEntityType().equals(EntityType.ACCOUNT_HOLDER))
                .map(AccountHolderDTO::new)
                .toList();

        return new BankAccountDTO(bankAccount, accountHolders);
    }
}
