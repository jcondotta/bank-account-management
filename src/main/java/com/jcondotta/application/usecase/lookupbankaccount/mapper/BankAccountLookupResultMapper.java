package com.jcondotta.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.domain.bankaccount.model.BankAccount;

public interface BankAccountLookupResultMapper {

    BankAccountLookupResult toResult(BankAccount bankAccount);
}
