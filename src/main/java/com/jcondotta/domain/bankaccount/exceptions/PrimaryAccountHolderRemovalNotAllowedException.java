package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.shared.exceptions.BusinessRuleException;

public class PrimaryAccountHolderRemovalNotAllowedException extends BusinessRuleException {

    public PrimaryAccountHolderRemovalNotAllowedException() {
        super("Only joint account holders can be added");
    }

    @Override
    public String getType() {
        return "/problems/primary-account-holder-missing";
    }
}