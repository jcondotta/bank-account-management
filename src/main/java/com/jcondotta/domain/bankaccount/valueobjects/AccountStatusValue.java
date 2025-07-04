package com.jcondotta.domain.bankaccount.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public static AccountStatusValue pending() {
        return new AccountStatusValue(AccountStatus.PENDING);
    }

    public static AccountStatusValue cancelled() {
        return new AccountStatusValue(AccountStatus.CANCELLED);
    }

    public static AccountStatusValue active() {
        return new AccountStatusValue(AccountStatus.ACTIVE);
    }

    public boolean is(AccountStatus status) {
        return value == status;
    }

    @JsonIgnore
    public boolean isPending() {
        return value == AccountStatus.PENDING;
    }

    @JsonIgnore
    public boolean isCancelled() {
        return value == AccountStatus.CANCELLED;
    }

    @JsonIgnore
    public boolean isActive() {
        return value == AccountStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return value.name();
    }
}
