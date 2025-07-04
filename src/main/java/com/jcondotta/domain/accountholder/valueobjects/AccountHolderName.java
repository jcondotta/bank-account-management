package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public record AccountHolderName(String value) {

    public static final int MAX_LENGTH = 255;

    public AccountHolderName {
        Objects.requireNonNull(value, ValidationErrors.AccountHolder.NAME_NOT_BLANK);

        var normalized = StringUtils.normalizeSpace(value).trim();

        if (StringUtils.isBlank(normalized)) {
            throw new IllegalArgumentException(ValidationErrors.AccountHolder.NAME_NOT_BLANK);
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(ValidationErrors.AccountHolder.NAME_TOO_LONG);
        }

        value = normalized;
    }

    public static AccountHolderName of(String value) {
        return new AccountHolderName(value);
    }

    public boolean equalsIgnoreCase(AccountHolderName other) {
        return this.value.equalsIgnoreCase(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}