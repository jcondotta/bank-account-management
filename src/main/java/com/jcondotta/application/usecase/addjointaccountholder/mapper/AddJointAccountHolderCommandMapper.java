package com.jcondotta.application.usecase.addjointaccountholder.mapper;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.model.AccountHolder;

public interface AddJointAccountHolderCommandMapper {

    AccountHolder toJointAccountHolder(CreateAccountHolderData createAccountHolderData);
}
