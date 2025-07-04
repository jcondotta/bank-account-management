package com.jcondotta.application.usecase.createbankaccount.mapper;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.domain.bankaccount.model.BankAccount;

public interface CreateBankAccountCommandMapper {

    BankAccount toBankAccount(CreateBankAccountCommand command);
}
