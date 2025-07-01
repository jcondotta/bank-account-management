package com.jcondotta.domain.accountholder.exceptions;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.exceptions.ResourceNotFoundException;
import lombok.Getter;

import java.io.Serializable;

import static com.jcondotta.domain.shared.DomainErrorMessages.ACCOUNT_HOLDER_NOT_FOUND;

@Getter
public class AccountHolderNotFoundException extends ResourceNotFoundException {

    public AccountHolderNotFoundException(BankAccountId bankAccountId, AccountHolderId accountHolderId) {
        super(ACCOUNT_HOLDER_NOT_FOUND, bankAccountId.value(), accountHolderId.value());
    }

    public AccountHolderNotFoundException(Serializable... identifier) {
        super(ACCOUNT_HOLDER_NOT_FOUND, identifier);
    }

    public AccountHolderNotFoundException(Throwable cause, Serializable... identifier) {
        super(ACCOUNT_HOLDER_NOT_FOUND, cause, identifier);
    }

    public AccountHolderNotFoundException(Throwable cause, BankAccountId bankAccountId, AccountHolderId accountHolderId) {
        super(ACCOUNT_HOLDER_NOT_FOUND, cause, bankAccountId.value(), accountHolderId.value());
    }
}