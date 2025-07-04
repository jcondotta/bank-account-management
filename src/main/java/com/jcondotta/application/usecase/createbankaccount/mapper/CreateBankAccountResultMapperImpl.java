package com.jcondotta.application.usecase.createbankaccount.mapper;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;
import com.jcondotta.application.usecase.shared.mapper.BankAccountDetailsMapper;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateBankAccountResultMapperImpl implements CreateBankAccountResultMapper {

    private final BankAccountDetailsMapper bankAccountDetailsMapper;

    @Override
    public CreateBankAccountResult toResult(BankAccount bankAccount) {
        return new CreateBankAccountResult(
            bankAccountDetailsMapper.toBankAccountDetails(bankAccount)
        );
    }
}
