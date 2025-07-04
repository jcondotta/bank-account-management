package com.jcondotta.interfaces.rest.addjointaccountholder.mapper;

import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;
import com.jcondotta.application.usecase.shared.model.AccountHolderDetails;
import com.jcondotta.interfaces.rest.addjointaccountholder.model.AddJointAccountHolderRestResponse;
import com.jcondotta.interfaces.rest.shared.AccountHolderDetailsRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddJointAccountHolderResponseControllerMapper {

    default AddJointAccountHolderRestResponse toResponse(AddJointAccountHolderResult addJointAccountHolderResult) {
        return new AddJointAccountHolderRestResponse(
            toAccountHolderDetailsResponse(addJointAccountHolderResult.accountHolder())
        );
    }

    default AccountHolderDetailsRestResponse toAccountHolderDetailsResponse(AccountHolderDetails accountHolderDetails){
        return AccountHolderDetailsRestResponse.builder()
            .accountHolderId(accountHolderDetails.accountHolderId().value())
            .accountHolderName(accountHolderDetails.accountHolderName().value())
            .passportNumber(accountHolderDetails.passportNumber().value())
            .dateOfBirth(accountHolderDetails.dateOfBirth().value())
            .accountHolderType(accountHolderDetails.accountHolderType().value())
            .createdAt(accountHolderDetails.createdAt().value())
            .build();
    }
}
