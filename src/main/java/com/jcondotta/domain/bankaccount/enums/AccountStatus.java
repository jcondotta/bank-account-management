package com.jcondotta.domain.bankaccount.enums;

public enum AccountStatus {
    PENDING, ACTIVE, CANCELLED;

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean is(AccountStatus accountStatus) {
        return this == accountStatus;
    }

    public boolean isNot(AccountStatus accountStatus) {
        return this != accountStatus;
    }
}