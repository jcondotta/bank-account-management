package com.jcondotta.application.usecase.addjointaccountholder.mapper;

import com.jcondotta.application.usecase.shared.mapper.AccountHolderMapper;
import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@AllArgsConstructor
public class AddJointAccountHolderCommandMapperImpl implements AddJointAccountHolderCommandMapper {

    private final AccountHolderMapper accountHolderMapper;

    @Override
    public AccountHolder toJointAccountHolder(CreateAccountHolderData createAccountHolderData) {
        return accountHolderMapper.toJointAccountHolder(createAccountHolderData);
    }
}
