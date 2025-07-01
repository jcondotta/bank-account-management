package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.DomainErrorMessages;
import lombok.Getter;

@Getter
public class AccountHolderAlreadyExistsException extends RuntimeException {

    private final BankAccountId bankAccountId;

    public AccountHolderAlreadyExistsException(BankAccountId bankAccountId) {
        super(DomainErrorMessages.ACCOUNT_HOLDER_ALREADY_EXISTS);
        this.bankAccountId = bankAccountId;
    }
}