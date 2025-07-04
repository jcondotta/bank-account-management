package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;
import java.util.UUID;

public record AccountHolderId(UUID value) {

    public AccountHolderId {
        Objects.requireNonNull(value, ValidationErrors.AccountHolder.ID_NOT_NULL);
    }

    public static AccountHolderId of(UUID value) {
        return new AccountHolderId(value);
    }

    public static AccountHolderId newId() {
        return new AccountHolderId(UUID.randomUUID());
    }

    public boolean is(UUID other) {
        return value.equals(other);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
