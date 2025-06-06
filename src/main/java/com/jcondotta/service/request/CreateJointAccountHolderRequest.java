package com.jcondotta.service.request;

import com.jcondotta.domain.Validatable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request containing information for creating joint account holder.")
public record CreateJointAccountHolderRequest(

        @NotNull(message = "bankAccount.bankAccountId.notNull")
        @Schema(description = "Bank account identifier", example = "01920bff-1338-7efd-ade6-e9128debe5d4")
        UUID bankAccountId,

        @Valid
        @NotNull(message = "bankAccount.accountHolder.notNull")
        @Schema(description = "Information about the new joint account holder")
        CreateAccountHolderRequest accountHolderRequest

) implements Validatable { }