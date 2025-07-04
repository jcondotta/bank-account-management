package com.jcondotta.interfaces.rest.lookupbankaccount.model;

import com.jcondotta.interfaces.rest.shared.BankAccountDetailsRestResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.*;

@Schema(description = "Response containing information for the looked-up bank account.")
public record BankAccountLookupRestResponse(

    @NotNull
    @Schema(description = "Details of the retrieved bank account.", requiredMode = RequiredMode.REQUIRED)
    BankAccountDetailsRestResponse bankAccount

) {}
