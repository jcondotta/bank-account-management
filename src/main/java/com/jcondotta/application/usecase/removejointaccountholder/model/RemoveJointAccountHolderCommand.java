package com.jcondotta.application.usecase.removejointaccountholder.model;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public record RemoveJointAccountHolderCommand(BankAccountId bankAccountId, AccountHolderId accountHolderId) { }