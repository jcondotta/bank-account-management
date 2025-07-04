package com.jcondotta.application.usecase.shared.mapper;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountDetailsMapperImpl implements BankAccountDetailsMapper {

    private final AccountHolderDetailsMapper accountHolderDetailsMapper;

    @Override
    public BankAccountDetails toBankAccountDetails(BankAccount bankAccount) {
        return new BankAccountDetails(
            bankAccount.bankAccountId(),
            bankAccount.accountType(),
            bankAccount.currency(),
            bankAccount.iban(),
            bankAccount.status(),
            bankAccount.createdAt(),
            bankAccount.accountHolders()
                .stream()
                .map(accountHolderDetailsMapper::toAccountHolderDetails)
                .toList()
        );
    }
}
