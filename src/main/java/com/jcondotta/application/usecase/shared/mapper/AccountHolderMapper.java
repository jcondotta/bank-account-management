package com.jcondotta.application.usecase.shared.mapper;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderTypeValue;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.time.Clock;
import java.util.UUID;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AccountHolderMapper {

    default AccountHolder toPrimaryAccountHolder(CreateAccountHolderData createAccountHolderData, Clock clock) {
        return AccountHolder.createPrimary(
            createAccountHolderData.accountHolderName(),
            createAccountHolderData.passportNumber(),
            createAccountHolderData.dateOfBirth(),
            CreatedAt.now(clock));
    }

    default AccountHolder toJointAccountHolder(CreateAccountHolderData createAccountHolderData) {
        return AccountHolder.createJoint(
            createAccountHolderData.accountHolderName(),
            createAccountHolderData.passportNumber(),
            createAccountHolderData.dateOfBirth());
    }
}