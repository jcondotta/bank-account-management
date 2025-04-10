package com.jcondotta.service.bank_account;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.EntityType;
import com.jcondotta.exception.BankAccountNotFoundException;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Singleton
public class FindBankAccountByIbanService {

    private final DynamoDbIndex<BankingEntity> bankingEntityIbanGSI;

    @Inject
    public FindBankAccountByIbanService(DynamoDbIndex<BankingEntity> bankingEntityIbanGSI) {
        this.bankingEntityIbanGSI = bankingEntityIbanGSI;
    }

    public BankAccountDTO findBankAccountByIban(@NotNull String bankAccountIban) {
        var query = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(bankAccountIban)
                .build());

        var resultsPage = bankingEntityIbanGSI.query(query).stream().toList();

        var bankingEntities = resultsPage.stream()
                .flatMap(page -> page.items().stream())
                .toList();

        var bankAccount = bankingEntities.stream()
                .filter(e -> EntityType.BANK_ACCOUNT.equals(e.getEntityType()))
                .findFirst()
                .orElseThrow(() -> new BankAccountNotFoundException("bankAccount.notFound", bankAccountIban));

        List<AccountHolderDTO> accountHolders = bankingEntities.stream()
                .filter(e -> EntityType.ACCOUNT_HOLDER.equals(e.getEntityType()))
                .map(AccountHolderDTO::new)
                .toList();

        return new BankAccountDTO(bankAccount, accountHolders);
    }
}
