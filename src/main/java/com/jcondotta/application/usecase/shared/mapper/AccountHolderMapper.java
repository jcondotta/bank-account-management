package com.jcondotta.application.usecase.shared.mapper;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AccountHolderMapper {

    default AccountHolder toPrimaryAccountHolder(CreateAccountHolderData createAccountHolderData) {
        return AccountHolder.createPrimary(
            createAccountHolderData.accountHolderName(),
            createAccountHolderData.passportNumber(),
            createAccountHolderData.dateOfBirth());
    }

    default AccountHolder toJointAccountHolder(CreateAccountHolderData createAccountHolderData) {
        return AccountHolder.createJoint(
            createAccountHolderData.accountHolderName(),
            createAccountHolderData.passportNumber(),
            createAccountHolderData.dateOfBirth());
    }
}