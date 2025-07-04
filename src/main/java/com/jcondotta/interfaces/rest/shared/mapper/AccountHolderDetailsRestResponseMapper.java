package com.jcondotta.interfaces.rest.shared.mapper;

import com.jcondotta.application.usecase.shared.model.AccountHolderDetails;
import com.jcondotta.interfaces.rest.shared.AccountHolderDetailsRestResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountHolderDetailsRestResponseMapper {

    default AccountHolderDetailsRestResponse toResponse(AccountHolderDetails holder) {
        return AccountHolderDetailsRestResponse.builder()
            .accountHolderId(holder.accountHolderId().value())
            .accountHolderName(holder.accountHolderName().value())
            .passportNumber(holder.passportNumber().value())
            .dateOfBirth(holder.dateOfBirth().value())
            .accountHolderType(holder.accountHolderType().value())
            .createdAt(holder.createdAt().value())
            .build();
    }
}
