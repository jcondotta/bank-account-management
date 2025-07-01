package com.jcondotta.interfaces.rest.lookup;

import com.jcondotta.interfaces.rest.BankAccountDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Represents a bank account entity with details.")
public record BankAccountLookupResponse(

    @NotNull
    BankAccountDetailsResponse bankAccount

) {}