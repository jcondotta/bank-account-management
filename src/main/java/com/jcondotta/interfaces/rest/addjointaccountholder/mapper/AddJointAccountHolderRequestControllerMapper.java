package com.jcondotta.interfaces.rest.addjointaccountholder.mapper;

import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.interfaces.rest.addjointaccountholder.model.AddJointAccountHolderRestRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddJointAccountHolderRequestControllerMapper {

    Logger LOGGER = LoggerFactory.getLogger(AddJointAccountHolderRequestControllerMapper.class);

    default AddJointAccountHolderCommand toUseCaseCommand(UUID bankAccountId, AddJointAccountHolderRestRequest restRequest) {
        return new AddJointAccountHolderCommand(
            BankAccountId.of(bankAccountId),
            new CreateAccountHolderData(
                AccountHolderName.of(restRequest.accountHolder().accountHolderName()),
                DateOfBirth.of(restRequest.accountHolder().dateOfBirth()),
                PassportNumber.of(restRequest.accountHolder().passportNumber())
            )
        );
    }
}
