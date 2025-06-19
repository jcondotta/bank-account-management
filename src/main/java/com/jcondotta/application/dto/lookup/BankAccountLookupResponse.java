package com.jcondotta.application.dto.lookup;

import com.jcondotta.application.dto.BankAccountDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Represents a bank account entity with details.")
public record BankAccountLookupResponse(

    @NotNull
    BankAccountDetailsResponse bankAccount

) {}