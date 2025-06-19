package com.jcondotta.application.dto.create;

import com.jcondotta.application.dto.AccountHolderDetailsRequest;
import com.jcondotta.application.dto.ValidatableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Builder
@Schema(description = "Request containing information for creating joint account holder.")
public record CreateJointAccountHolderRequest(

    @Valid
    @NotNull(message = "bankAccount.accountHolder.notNull")
    @Schema(description = "Information about the new joint account holder", requiredMode = RequiredMode.REQUIRED)
    AccountHolderDetailsRequest accountHolder

) implements ValidatableRequest { }