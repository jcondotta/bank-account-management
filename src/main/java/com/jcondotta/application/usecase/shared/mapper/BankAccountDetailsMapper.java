package com.jcondotta.application.usecase.shared.mapper;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.domain.bankaccount.model.BankAccount;

public interface BankAccountDetailsMapper {

    BankAccountDetails toBankAccountDetails(BankAccount bankAccount);
}
