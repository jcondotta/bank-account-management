package com.jcondotta.service.bank_account;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.BankingEntityMapper;
import com.jcondotta.domain.EntityType;
import com.jcondotta.exception.ResourceNotFoundException;
import com.jcondotta.service.dto.BankAccountDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.UUID;

@Service
public class FindBankAccountService {

    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final BankingEntityMapper bankingEntityMapper;


    public FindBankAccountService(DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable, BankingEntityMapper bankingEntityMapper) {
        this.bankingEntityDynamoDbTable = bankingEntityDynamoDbTable;
        this.bankingEntityMapper = bankingEntityMapper;
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
                .orElseThrow(() -> new ResourceNotFoundException(bankAccountId, "bankAccount.notFound"));

        return bankingEntityMapper.toDto(bankAccount, bankingEntities.stream()
                .filter(bankingEntity -> bankingEntity.getEntityType().equals(EntityType.ACCOUNT_HOLDER))
                .toList());
    }
}
