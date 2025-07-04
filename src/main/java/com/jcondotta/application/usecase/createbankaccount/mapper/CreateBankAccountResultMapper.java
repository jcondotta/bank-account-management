package com.jcondotta.application.usecase.createbankaccount.mapper;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;
import com.jcondotta.domain.bankaccount.model.BankAccount;

public interface CreateBankAccountResultMapper {

    CreateBankAccountResult toResult(BankAccount bankAccount);
}

