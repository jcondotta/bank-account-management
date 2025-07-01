package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;

public record AccountTypeValue(AccountType value) {

    public AccountTypeValue {
        Objects.requireNonNull(value, ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL);
    }

    public static AccountTypeValue of(AccountType value) {
        return new AccountTypeValue(value);
    }
}
