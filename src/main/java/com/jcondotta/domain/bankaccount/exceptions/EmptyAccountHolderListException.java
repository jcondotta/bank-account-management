package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.shared.DomainErrorMessages;
import com.jcondotta.domain.shared.exceptions.BusinessRuleException;

public class EmptyAccountHolderListException extends BusinessRuleException {

    public EmptyAccountHolderListException() {
        super(DomainErrorMessages.PRIMARY_ACCOUNT_HOLDERS_MISSING);
    }

    @Override
    public String getType() {
        return "/problems/empty-account-holder-list";
    }
}