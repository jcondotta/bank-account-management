package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;

public record AccountHolderTypeValue(AccountHolderType value) {

    public AccountHolderTypeValue {
        Objects.requireNonNull(value, ValidationErrors.AccountHolder.ACCOUNT_HOLDER_TYPE_NOT_NULL);
    }

    public static AccountHolderTypeValue of(AccountHolderType value) {
        return new AccountHolderTypeValue(value);
    }
}
