package com.jcondotta.interfaces.rest.addjointaccountholder.model;

import com.jcondotta.interfaces.rest.shared.AccountHolderDetailsRestResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Response containing information for the created joint account holder.")
public record AddJointAccountHolderRestResponse(

    @NotNull
    @Schema(description = "Details of the created joint account holder", requiredMode = Schema.RequiredMode.REQUIRED)
    AccountHolderDetailsRestResponse accountHolder

){ }