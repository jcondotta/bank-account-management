package com.jcondotta.interfaces.rest.shared.mapper;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.interfaces.rest.shared.BankAccountDetailsRestResponse;

public interface BankAccountDetailsRestResponseMapper {

    BankAccountDetailsRestResponse toResponse(BankAccountDetails bankAccount);
}
