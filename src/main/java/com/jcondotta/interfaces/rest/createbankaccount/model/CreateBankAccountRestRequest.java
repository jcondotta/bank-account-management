package com.jcondotta.interfaces.rest.createbankaccount.model;

import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.enums.Currency;
import com.jcondotta.interfaces.rest.shared.ValidatableRequest;
import com.jcondotta.interfaces.rest.shared.CreateAccountHolderRestRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Request object used for creating a new bank account with a primary account holder.")
public record CreateBankAccountRestRequest(

    @NotNull(message = ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL)
    @Schema(description = "Type of bank account (e.g., SAVINGS, CHECKING)", example = "SAVINGS",
        requiredMode = RequiredMode.REQUIRED, allowableValues = { "SAVINGS", "CHECKING "})
    AccountType accountType,

    @NotNull(message = ValidationErrors.BankAccount.CURRENCY_NOT_NULL)
    @Schema(description = "Currency for the bank account (e.g., USD, EUR)", example = "USD", requiredMode = RequiredMode.REQUIRED)
    Currency currency,

    @Valid
    @NotNull(message = ValidationErrors.BankAccount.ACCOUNT_HOLDER_NOT_NULL)
    @Schema(description = "Information about the primary account holder", requiredMode = RequiredMode.REQUIRED)
    CreateAccountHolderRestRequest accountHolder

) implements ValidatableRequest { }
