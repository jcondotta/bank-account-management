package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.DomainErrorMessages;
import com.jcondotta.domain.shared.exceptions.BusinessRuleException;

public class AccountHolderAlreadyExistsException extends BusinessRuleException {

    public AccountHolderAlreadyExistsException(BankAccountId bankAccountId) {
        super(DomainErrorMessages.ACCOUNT_HOLDER_ALREADY_EXISTS);
    }

    @Override
    public String getType() {
        return "/problems/account-holder-already-exists";
    }
}