package com.jcondotta.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.application.usecase.lookupbankaccount.model.LookupBankAccountResult;
import com.jcondotta.domain.bankaccount.model.BankAccount;

public interface LookupBankAccountResultMapper {

    LookupBankAccountResult toResult(BankAccount bankAccount);
}
