package com.jcondotta.domain.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BankAccountNotFoundException extends ResourceNotFoundException {

    public static final String BANK_ACCOUNT_NOT_FOUND_MESSAGE = "bankAccount.notFound";

    public BankAccountNotFoundException(Serializable identifier) {
        super(BANK_ACCOUNT_NOT_FOUND_MESSAGE, identifier);
    }

    public BankAccountNotFoundException(Throwable cause, Serializable identifier) {
        super(BANK_ACCOUNT_NOT_FOUND_MESSAGE, cause, identifier);
    }
}
