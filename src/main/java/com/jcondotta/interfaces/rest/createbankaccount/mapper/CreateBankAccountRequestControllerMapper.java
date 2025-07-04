package com.jcondotta.interfaces.rest.createbankaccount.mapper;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;
import com.jcondotta.interfaces.rest.createbankaccount.model.CreateBankAccountRestRequest;
import com.jcondotta.interfaces.rest.shared.CreateAccountHolderRestRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateBankAccountRequestControllerMapper {

    default CreateBankAccountCommand toUseCaseCommand(CreateBankAccountRestRequest restRequest) {
        return new CreateBankAccountCommand(
            AccountTypeValue.of(restRequest.accountType()),
            CurrencyValue.of(restRequest.currency()),
            toCreateAccountHolderRequest(restRequest.accountHolder())
        );
    }

    default CreateAccountHolderData toCreateAccountHolderRequest(CreateAccountHolderRestRequest request) {
        return new CreateAccountHolderData(
            AccountHolderName.of(request.accountHolderName()),
            DateOfBirth.of(request.dateOfBirth()),
            PassportNumber.of(request.passportNumber())
        );
    }
}
