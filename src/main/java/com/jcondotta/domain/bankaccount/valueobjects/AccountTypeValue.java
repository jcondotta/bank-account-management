package com.jcondotta.domain.bankaccount.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public static AccountTypeValue checking() {
        return new AccountTypeValue(AccountType.CHECKING);
    }

    public static AccountTypeValue savings() {
        return new AccountTypeValue(AccountType.SAVINGS);
    }

    public boolean is(AccountType type) {
        return value == type;
    }

    @JsonIgnore
    public boolean isChecking() {
        return value == AccountType.CHECKING;
    }

    @JsonIgnore
    public boolean isSavings() {
        return value == AccountType.SAVINGS;
    }

    @Override
    public String toString() {
        return value.name();
    }
}
