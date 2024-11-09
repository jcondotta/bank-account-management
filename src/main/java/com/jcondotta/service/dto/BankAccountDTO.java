package com.jcondotta.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcondotta.domain.BankAccount;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Serdeable
@Schema(name = "BankAccountDTO", description = "Represents a bank account entity with details.")
public class BankAccountDTO {

    @NotNull
    @Schema(description = "Unique identifier for the bank account.",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
            requiredMode = RequiredMode.REQUIRED)
    private UUID bankAccountId;

    @NotNull
    @Schema(description = "Main account holder associated with this bank account.",
            requiredMode = RequiredMode.REQUIRED)
    private AccountHolderDTO accountHolder;

    @NotBlank
    @Schema(description = "International Bank Account Number (IBAN) for the bank account.",
            example = "GB29NWBK60161331926819",
            maxLength = 34,
            requiredMode = RequiredMode.REQUIRED)
    private String iban;

    @NotNull
    @Schema(description = "Date and time when the bank account was opened.",
            example = "2023-08-23T14:55:00Z",
            requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime dateOfOpening;

    public BankAccountDTO(
            @JsonProperty("bankAccountId") UUID bankAccountId,
            @JsonProperty("accountHolder") AccountHolderDTO accountHolder,
            @JsonProperty("iban") String iban,
            @JsonProperty("dateOfOpening") LocalDateTime dateOfOpening) {
        this.bankAccountId = bankAccountId;
        this.accountHolder = accountHolder;
        this.iban = iban;
        this.dateOfOpening = dateOfOpening;
    }

    public BankAccountDTO(BankAccount bankAccount) {
        this(bankAccount.getBankAccountId(),
            new AccountHolderDTO(bankAccount.getAccountHolderName(), bankAccount.getDateOfBirth(), bankAccount.getPassportNumber()),
            bankAccount.getIban(),
            bankAccount.getDateOfOpening());
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public AccountHolderDTO getAccountHolder() {
        return accountHolder;
    }

    public String getIban() {
        return iban;
    }

    public LocalDateTime getDateOfOpening() {
        return dateOfOpening;
    }
}
