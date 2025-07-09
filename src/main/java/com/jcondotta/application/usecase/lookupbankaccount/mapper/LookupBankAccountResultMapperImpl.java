package com.jcondotta.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.application.usecase.lookupbankaccount.model.LookupBankAccountResult;
import com.jcondotta.application.usecase.shared.mapper.BankAccountDetailsMapper;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LookupBankAccountResultMapperImpl implements LookupBankAccountResultMapper {

    private final BankAccountDetailsMapper bankAccountDetailsMapper;

    @Override
    public LookupBankAccountResult toResult(BankAccount bankAccount) {
        return new LookupBankAccountResult(
            bankAccountDetailsMapper.toBankAccountDetails(bankAccount)
        );
    }
}
