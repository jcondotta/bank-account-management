package com.jcondotta.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.application.usecase.shared.mapper.BankAccountDetailsMapper;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountLookupResultMapperImpl implements BankAccountLookupResultMapper {

    private final BankAccountDetailsMapper bankAccountDetailsMapper;

    @Override
    public BankAccountLookupResult toResult(BankAccount bankAccount) {
        return new BankAccountLookupResult(
            bankAccountDetailsMapper.toBankAccountDetails(bankAccount)
        );
    }
}
