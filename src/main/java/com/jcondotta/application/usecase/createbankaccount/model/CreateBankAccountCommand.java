package com.jcondotta.application.usecase.createbankaccount.model;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;

public record CreateBankAccountCommand (
    AccountTypeValue accountType,
    CurrencyValue currency,
    CreateAccountHolderData createAccountHolderData
) {}