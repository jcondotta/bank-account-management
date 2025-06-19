package com.jcondotta.application.dto.create;

import com.jcondotta.application.dto.BankAccountDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Represents a bank account entity with details.")
public record CreateBankAccountResponse(

    @NotNull
    BankAccountDetailsResponse bankAccount
) {}