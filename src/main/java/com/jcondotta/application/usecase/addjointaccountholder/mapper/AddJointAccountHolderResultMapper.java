package com.jcondotta.application.usecase.addjointaccountholder.mapper;

import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;
import com.jcondotta.domain.accountholder.model.AccountHolder;

public interface AddJointAccountHolderResultMapper {

    AddJointAccountHolderResult toResult(AccountHolder jointAccountHolder);
}