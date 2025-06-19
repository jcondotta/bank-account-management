package com.jcondotta.domain.value_object;

import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

import static com.jcondotta.domain.exception.AccountHolderNotFoundException.ACCOUNT_HOLDER_NOT_FOUND_MESSAGE;

@Builder
public record AccountHolderId(UUID value) {

    public AccountHolderId {
        Objects.requireNonNull(value, ACCOUNT_HOLDER_NOT_FOUND_MESSAGE);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}