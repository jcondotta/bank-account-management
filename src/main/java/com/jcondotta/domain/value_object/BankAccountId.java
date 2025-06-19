package com.jcondotta.domain.value_object;

import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

import static com.jcondotta.domain.exception.BankAccountNotFoundException.BANK_ACCOUNT_NOT_FOUND_MESSAGE;

@Builder
public record BankAccountId(UUID value) {

    public BankAccountId {
        Objects.requireNonNull(value, BANK_ACCOUNT_NOT_FOUND_MESSAGE);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}