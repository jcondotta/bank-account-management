package com.jcondotta.exception;

import java.util.UUID;

public class BankAccountNotFoundException extends RuntimeException{

    private final UUID bankAccountId;

    public BankAccountNotFoundException(String message, UUID bankAccountId) {
        super(message);
        this.bankAccountId = bankAccountId;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }
}
