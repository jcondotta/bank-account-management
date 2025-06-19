package com.jcondotta.domain.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AccountHolderNotFoundException extends ResourceNotFoundException {

    public static final String ACCOUNT_HOLDER_NOT_FOUND_MESSAGE = "accountHolder.notFound";

    public AccountHolderNotFoundException(Serializable... identifier) {
        super(ACCOUNT_HOLDER_NOT_FOUND_MESSAGE, identifier);
    }

    public AccountHolderNotFoundException(Throwable cause, Serializable... identifier) {
        super(ACCOUNT_HOLDER_NOT_FOUND_MESSAGE, cause, identifier);
    }
}
