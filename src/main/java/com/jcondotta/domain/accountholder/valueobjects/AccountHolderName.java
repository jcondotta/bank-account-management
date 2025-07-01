package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import org.apache.commons.lang3.StringUtils;

public record AccountHolderName(String value) {

    private static final int MAX_LENGTH = 255;

    public AccountHolderName {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(ValidationErrors.AccountHolder.NAME_BLANK);
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(ValidationErrors.AccountHolder.NAME_TOO_LONG);
        }
    }

    public static AccountHolderName of(String value) {
        return new AccountHolderName(value);
    }
}
