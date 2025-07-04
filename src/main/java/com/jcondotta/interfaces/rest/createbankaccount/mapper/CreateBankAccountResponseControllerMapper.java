package com.jcondotta.interfaces.rest.createbankaccount.mapper;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.interfaces.rest.createbankaccount.model.CreateBankAccountRestResponse;

public interface CreateBankAccountResponseControllerMapper {

    CreateBankAccountRestResponse toResponse(BankAccountDetails bankAccountDetails);
}