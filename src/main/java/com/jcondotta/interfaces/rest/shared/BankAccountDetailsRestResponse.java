package com.jcondotta.interfaces.rest.shared;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "BankAccountDetailsResponse", description = "Represents the details of an bank account.")
public class BankAccountDetailsRestResponse {

    @NotNull
    @Schema(description = "The UUID value representing the bank account identifier.",
        example = "01920bff-1338-7efd-ade6-e9128debe5d4",
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
    AccountStatus status;

    @NotNull
    @Schema(description = "Account holders associated with this bank account.", requiredMode = RequiredMode.REQUIRED)
    List<AccountHolderDetailsRestResponse> accountHolders;
}