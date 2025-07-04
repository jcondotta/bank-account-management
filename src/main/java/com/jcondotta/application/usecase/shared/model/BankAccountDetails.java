package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;

import java.util.List;

public record BankAccountDetails (
    BankAccountId bankAccountId,
    AccountTypeValue accountType,
    CurrencyValue currency,
    Iban iban,
    AccountStatusValue accountStatus,
    CreatedAt dateOfOpening,
    List<AccountHolderDetails> accountHolders
) {}