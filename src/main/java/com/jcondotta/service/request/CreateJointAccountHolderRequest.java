package com.jcondotta.service.request;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Serdeable
@Schema(description = "Request containing information for creating joint account holders.")
public record CreateJointAccountHolderRequest(

        @NotNull(message = "bankAccount.bankAccountId.notNull")
        UUID bankAccountId,

        @Valid
        @NotEmpty(message = "accountHolders.notEmpty")
        List<AccountHolderRequest> accountHolderRequests) {

    public CreateJointAccountHolderRequest(UUID bankAccountId, AccountHolderRequest accountHolderRequest) {
        this(bankAccountId, List.of(accountHolderRequest));
    }
}
