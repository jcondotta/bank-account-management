package com.jcondotta.domain.accountholder.valueobjects;

import java.util.Objects;
import java.util.UUID;

import com.jcondotta.domain.shared.ValidationErrors;

public record AccountHolderId(UUID value) {

    public AccountHolderId {
        Objects.requireNonNull(value, ValidationErrors.AccountHolder.ID_NOT_NULL);
    }

    public static AccountHolderId of(UUID value) {
        return new AccountHolderId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
