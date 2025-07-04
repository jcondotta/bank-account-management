package com.jcondotta.application.usecase.addjointaccountholder.model;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public record AddJointAccountHolderCommand(BankAccountId bankAccountId, CreateAccountHolderData createAccountHolderData) { }