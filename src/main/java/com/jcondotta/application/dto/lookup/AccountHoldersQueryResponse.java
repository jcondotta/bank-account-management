package com.jcondotta.application.dto.lookup;

import com.jcondotta.application.dto.AccountHolderDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Builder
@Schema(description = "Represents detailed information about the account holders associated with a bank account.")
public record AccountHoldersQueryResponse(

    @NotNull
    @Schema(description = "Account holders associated with this bank account.", requiredMode = RequiredMode.REQUIRED)
    List<AccountHolderDetailsResponse> accountHolders
) {}