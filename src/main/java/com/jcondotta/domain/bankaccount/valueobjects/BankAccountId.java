package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;
import java.util.UUID;

public record BankAccountId(UUID value) {

    public BankAccountId {
        Objects.requireNonNull(value, ValidationErrors.BankAccount.ID_NOT_NULL);
    }

    public static BankAccountId of(UUID value) {
        return new BankAccountId(value);
    }

    public static BankAccountId newId() {
        return new BankAccountId(UUID.randomUUID());
    }

    public boolean is(UUID other) {
        return value.equals(other);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
