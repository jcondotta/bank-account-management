package com.jcondotta.service.request;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Serdeable
@Schema(description = "Request to create a new bank account with associated primary account holder.")
public record CreateBankAccountRequest(

        @Schema(
                description = "Primary account holder associated with the bank account.",
                requiredMode = RequiredMode.REQUIRED,
                implementation = AccountHolderRequest.class
        )
        @Valid
        @NotNull(message = "bankAccount.accountHolder.notNull")
        AccountHolderRequest accountHolder
) {
}
