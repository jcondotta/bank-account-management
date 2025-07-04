package com.jcondotta.application.usecase.createbankaccount.model;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;

public record CreateBankAccountResult (BankAccountDetails bankAccount) {}