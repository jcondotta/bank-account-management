package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;

import java.util.List;

public record BankAccountDetails (
    BankAccountId bankAccountId,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus accountStatus,
    CreatedAt dateOfOpening,
    List<AccountHolderDetails> accountHolders
) {}