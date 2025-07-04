package com.jcondotta.application.usecase.shared.mapper;

import com.jcondotta.application.usecase.shared.model.AccountHolderDetails;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AccountHolderDetailsMapper {

    AccountHolderDetails toAccountHolderDetails(AccountHolder accountHolder);
}