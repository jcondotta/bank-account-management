package com.jcondotta.interfaces.rest.lookupbankaccount.mapper;

import com.jcondotta.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.interfaces.rest.lookupbankaccount.model.BankAccountLookupRestResponse;
import com.jcondotta.interfaces.rest.shared.mapper.BankAccountDetailsRestResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountLookupResponseControllerMapperImpl implements BankAccountLookupResponseControllerMapper {

    private final BankAccountDetailsRestResponseMapper bankAccountDetailsRestResponseMapper;

    public BankAccountLookupRestResponse toResponse(BankAccountLookupResult result) {
        return new BankAccountLookupRestResponse(
            bankAccountDetailsRestResponseMapper.toResponse(result.bankAccountDetails())
        );
    }
}
