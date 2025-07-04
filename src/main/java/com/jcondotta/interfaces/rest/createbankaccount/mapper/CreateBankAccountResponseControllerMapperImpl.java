package com.jcondotta.interfaces.rest.createbankaccount.mapper;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.interfaces.rest.createbankaccount.model.CreateBankAccountRestResponse;
import com.jcondotta.interfaces.rest.shared.mapper.BankAccountDetailsRestResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateBankAccountResponseControllerMapperImpl implements CreateBankAccountResponseControllerMapper{

    private final BankAccountDetailsRestResponseMapper bankAccountDetailsRestResponseMapper;

    public CreateBankAccountRestResponse toResponse(BankAccountDetails bankAccountDetails) {
        return new CreateBankAccountRestResponse(
            bankAccountDetailsRestResponseMapper.toResponse(bankAccountDetails)
        );
    }
}