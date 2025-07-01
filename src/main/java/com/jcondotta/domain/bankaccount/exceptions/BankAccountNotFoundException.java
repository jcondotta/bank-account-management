package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.exceptions.ResourceNotFoundException;
import lombok.Getter;

import java.io.Serializable;

import static com.jcondotta.domain.shared.DomainErrorMessages.BANK_ACCOUNT_NOT_FOUND_MESSAGE;

@Getter
public class BankAccountNotFoundException extends ResourceNotFoundException {

    public BankAccountNotFoundException(BankAccountId bankAccountId) {
        super(BANK_ACCOUNT_NOT_FOUND_MESSAGE, bankAccountId.value());
    }

    public BankAccountNotFoundException(Serializable identifier) {
        super(BANK_ACCOUNT_NOT_FOUND_MESSAGE, identifier);
    }

    public BankAccountNotFoundException(Throwable cause, Serializable identifier) {
        super(BANK_ACCOUNT_NOT_FOUND_MESSAGE, cause, identifier);
    }

    public BankAccountNotFoundException(Throwable cause, BankAccountId bankAccountId) {
        super(BANK_ACCOUNT_NOT_FOUND_MESSAGE, cause, bankAccountId.value());
    }
}
