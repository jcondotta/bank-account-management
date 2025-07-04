package com.jcondotta.interfaces.rest.addjointaccountholder.model;

import com.jcondotta.interfaces.rest.shared.ValidatableRequest;
import com.jcondotta.interfaces.rest.shared.CreateAccountHolderRestRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Request containing information for creating joint account holder.")
public record AddJointAccountHolderRestRequest(

    @Valid
    @NotNull(message = "bankAccount.accountHolder.notNull")
    @Schema(description = "Information about the new joint account holder", requiredMode = RequiredMode.REQUIRED)
    CreateAccountHolderRestRequest accountHolder

) implements ValidatableRequest { }