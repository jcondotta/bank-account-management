package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;

public record AccountStatusValue(AccountStatus value) {

    public AccountStatusValue {
        Objects.requireNonNull(value, ValidationErrors.BankAccount.STATUS_NOT_NULL);
    }

    public static AccountStatusValue of(AccountStatus value) {
        return new AccountStatusValue(value);
    }
}
