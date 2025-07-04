package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.shared.DomainErrorMessages;
import com.jcondotta.domain.shared.exceptions.BusinessRuleException;

public class NoPrimaryAccountHolderException extends BusinessRuleException {

    public NoPrimaryAccountHolderException() {
        super(DomainErrorMessages.PRIMARY_ACCOUNT_HOLDERS_MISSING);
    }

    @Override
    public String getType() {
        return "/problems/primary-account-holder-missing";
    }
}