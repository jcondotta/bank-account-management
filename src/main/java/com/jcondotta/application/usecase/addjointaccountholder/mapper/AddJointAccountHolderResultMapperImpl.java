package com.jcondotta.application.usecase.addjointaccountholder.mapper;

import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;
import com.jcondotta.application.usecase.shared.mapper.AccountHolderDetailsMapper;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddJointAccountHolderResultMapperImpl implements AddJointAccountHolderResultMapper {

    private final AccountHolderDetailsMapper accountHolderDetailsMapper;

    @Override
    public AddJointAccountHolderResult toResult(AccountHolder jointAccountHolder) {
        return new AddJointAccountHolderResult(
            accountHolderDetailsMapper.toAccountHolderDetails(jointAccountHolder)
        );
    }
}