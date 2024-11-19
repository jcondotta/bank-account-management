package com.jcondotta.service.request;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@Serdeable
@Schema(description = "Request containing information for creating joint account holders.")
public record CreateJointAccountHoldersRequest(

        @NotNull(message = "bankAccount.bankAccountId.notNull")
        UUID bankAccountId,

        @Valid
        @Size(max = 2, message = "accountHolders.tooMany")
        @NotEmpty(message = "accountHolders.notEmpty")
        List< //@NotNull(message = "accountHolder.notNull")
                AccountHolderRequest> accountHolderRequests) {

    public CreateJointAccountHoldersRequest(UUID bankAccountId, AccountHolderRequest accountHolderRequest) {
        this(bankAccountId, List.of(accountHolderRequest));
    }
}
