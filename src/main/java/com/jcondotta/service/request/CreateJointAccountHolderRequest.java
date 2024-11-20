package com.jcondotta.service.request;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Serdeable
@Schema(description = "Request containing information for creating joint account holder.")
public record CreateJointAccountHolderRequest(

        @NotNull(message = "bankAccount.bankAccountId.notNull")
        UUID bankAccountId,

        @Valid
        @NotNull(message = "bankAccount.accountHolder.notNull")
        AccountHolderRequest accountHolderRequest) {
}
