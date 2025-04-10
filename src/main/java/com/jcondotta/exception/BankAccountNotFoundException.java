package com.jcondotta.exception;

public class BankAccountNotFoundException extends RuntimeException{

    private final String bankAccountId;

    public BankAccountNotFoundException(String message, String bankAccountId) {
        super(message);
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }
}
