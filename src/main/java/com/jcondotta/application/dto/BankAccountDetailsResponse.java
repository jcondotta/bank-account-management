package com.jcondotta.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.domain.model.AccountType;
import com.jcondotta.domain.model.BankAccountStatus;
import com.jcondotta.domain.model.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "BankAccountDetailsResponse", description = "Represents the details of an bank account.")
public class BankAccountDetailsResponse {

    @NotNull
    @Schema(description = "The UUID value representing the bank account identifier.",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        requiredMode = Schema.RequiredMode.REQUIRED)
    UUID bankAccountId;

    @NotNull
    @Schema(description = "Type of bank account (e.g., SAVINGS, CHECKING)", example = "SAVINGS",
        requiredMode = RequiredMode.REQUIRED, allowableValues = { "SAVINGS", "CHECKING "})
    AccountType accountType;

    @NotNull
    @Schema(description = "Currency for the bank account (e.g., USD, EUR)", example = "USD", requiredMode = RequiredMode.REQUIRED)
    Currency currency;

    @NotBlank
    @Schema(description = "International Bank Account Number (IBAN) for the bank account.",
        example = "GB29NWBK60161331926819",
        maxLength = 34,
        requiredMode = RequiredMode.REQUIRED)
    String iban;

    @NotNull
    @Schema(description = "Date and time when the bank account was opened.",
        example = "2023-08-23T14:55:00Z",
        requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    LocalDateTime dateOfOpening;

    @NotNull
    @Schema(description = "Current status of the bank account", allowableValues = {"PENDING", "ACTIVE", "CANCELLED"})
    BankAccountStatus status;

    @NotNull
    @Schema(description = "Account holders associated with this bank account.", requiredMode = RequiredMode.REQUIRED)
    List<AccountHolderDetailsResponse> accountHolders;
}