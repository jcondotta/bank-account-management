package com.jcondotta.domain.accountholder.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public static AccountHolderTypeValue primary() {
        return new AccountHolderTypeValue(AccountHolderType.PRIMARY);
    }

    public static AccountHolderTypeValue joint() {
        return new AccountHolderTypeValue(AccountHolderType.JOINT);
    }

    public boolean is(AccountHolderType type) {
        return value == type;
    }

    @JsonIgnore
    public boolean isPrimary() {
        return value == AccountHolderType.PRIMARY;
    }

    @JsonIgnore
    public boolean isJoint() {
        return value == AccountHolderType.JOINT;
    }

    @Override
    public String toString() {
        return value.name();
    }
}