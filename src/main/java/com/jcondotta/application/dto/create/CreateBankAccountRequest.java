package com.jcondotta.application.dto.create;

import com.jcondotta.application.dto.AccountHolderDetailsRequest;
import com.jcondotta.application.dto.ValidatableRequest;
import com.jcondotta.domain.model.AccountType;
import com.jcondotta.domain.model.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Builder
@Schema(description = "Request object used for creating a new bank account with a primary account holder.")
public record CreateBankAccountRequest(

    @NotNull(message = "bankAccount.accountType.notNull")
    @Schema(description = "Type of bank account (e.g., SAVINGS, CHECKING)", example = "SAVINGS",
        requiredMode = RequiredMode.REQUIRED, allowableValues = { "SAVINGS", "CHECKING "})
    AccountType accountType,

    @NotNull(message = "bankAccount.currency.notNull")
    @Schema(description = "Currency for the bank account (e.g., USD, EUR)", example = "USD", requiredMode = RequiredMode.REQUIRED)
    Currency currency,

    @Valid
    @NotNull(message = "bankAccount.accountHolder.notNull")
    @Schema(description = "Information about the primary account holder", requiredMode = RequiredMode.REQUIRED)
    AccountHolderDetailsRequest accountHolder

) implements ValidatableRequest { }
