package com.jcondotta.interfaces.rest.lookupbankaccount.mapper;

import com.jcondotta.application.usecase.lookupbankaccount.model.LookupBankAccountResult;
import com.jcondotta.interfaces.rest.lookupbankaccount.model.BankAccountLookupRestResponse;

public interface BankAccountLookupResponseControllerMapper {

    BankAccountLookupRestResponse toResponse(LookupBankAccountResult result);
}
