package com.jcondotta.interfaces.rest.removejointaccountholder.mapper;

import com.jcondotta.application.usecase.removejointaccountholder.model.RemoveJointAccountHolderCommand;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RemoveJointAccountHolderRequestControllerMapper {

    Logger LOGGER = LoggerFactory.getLogger(RemoveJointAccountHolderRequestControllerMapper.class);

    default RemoveJointAccountHolderCommand toUseCaseCommand(UUID bankAccountId, UUID accountHolderId) {
        return new RemoveJointAccountHolderCommand(
            BankAccountId.of(bankAccountId),
            AccountHolderId.of(accountHolderId)
        );
    }
}
