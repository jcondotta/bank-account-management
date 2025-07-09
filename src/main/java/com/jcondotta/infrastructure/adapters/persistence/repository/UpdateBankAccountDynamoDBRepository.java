package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountEntityKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.BankAccountEntityMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class UpdateBankAccountDynamoDBRepository implements UpdateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateBankAccountDynamoDBRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final BankAccountEntityMapper bankAccountEntityMapper;

    @Override
    public void update(BankAccount bankAccount) {
        LOGGER.info("Updating bank account and holders for ID: {}", bankAccount.bankAccountId());

        // Novas entidades geradas a partir do estado atual no domínio
        var newEntities = bankAccountEntityMapper.toEntities(bankAccount);

        // Obtem os SKs das novas entidades (normalmente: BANK_ACCOUNT, ACCOUNT_HOLDER#xxx, etc.)
        Set<String> currentSkSet = newEntities.stream()
            .map(BankingEntity::getSortKey) // ou getSk()
            .collect(Collectors.toSet());

        // Consulta todos os registros atuais no DynamoDB para a PK da conta
        List<BankingEntity> existingEntities = bankingEntityDynamoDbTable
            .query(r -> r.queryConditional(QueryConditional.keyEqualTo(
                Key.builder().partitionValue(BankAccountEntityKey.partitionKey(bankAccount.bankAccountId())).build()
            )))
            .items()
            .stream()
            .toList();

        // Determina quais SKs devem ser removidos
        List<BankingEntity> toDelete = existingEntities.stream()
            .filter(entity -> !currentSkSet.contains(entity.getSortKey()))
            .toList();

        // Monta transação com put + delete
        var txBuilder = TransactWriteItemsEnhancedRequest.builder();

        newEntities.forEach(entity ->
            txBuilder.addPutItem(bankingEntityDynamoDbTable, entity)
        );

        toDelete.forEach(entity ->
            txBuilder.addDeleteItem(bankingEntityDynamoDbTable,
                Key.builder()
                    .partitionValue(entity.getPartitionKey())
                    .sortValue(entity.getSortKey())
                    .build()
            )
        );

        // Executa a transação
        dynamoDbEnhancedClient.transactWriteItems(txBuilder.build());

        LOGGER.debug("Successfully updated bank account and holders for ID: {}", bankAccount.bankAccountId());
    }

}