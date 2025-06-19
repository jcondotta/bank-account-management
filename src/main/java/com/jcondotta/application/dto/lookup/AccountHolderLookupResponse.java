package com.jcondotta.application.dto.lookup;

import com.jcondotta.application.dto.AccountHolderDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Builder
@Schema(description = "Represents a account holder entity with details.")
public record AccountHolderLookupResponse(

    @NotNull
    @Schema(description = "Represents the details of an account holder.", requiredMode = RequiredMode.REQUIRED)
    AccountHolderDetailsResponse accountHolder

) {}