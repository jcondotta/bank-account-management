package com.jcondotta.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.BankAccount;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
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
    @Schema(description = "Account holders associated with this bank account.",
            requiredMode = RequiredMode.REQUIRED)
    private List<AccountHolderDTO> accountHolders;

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

    public BankAccountDTO() {}

    public BankAccountDTO(UUID bankAccountId, List<AccountHolderDTO> accountHolders, String iban, LocalDateTime dateOfOpening) {
        this.bankAccountId = bankAccountId;
        this.accountHolders = accountHolders;
        this.iban = iban;
        this.dateOfOpening = dateOfOpening;
    }

    public BankAccountDTO(BankAccount bankAccount, AccountHolder accountHolder) {
        this(bankAccount.getBankAccountId(), List.of(new AccountHolderDTO(accountHolder)), bankAccount.getIban(), bankAccount.getDateOfOpening());
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public List<AccountHolderDTO> getAccountHolders() {
        return accountHolders;
    }

    public String getIban() {
        return iban;
    }

    public LocalDateTime getDateOfOpening() {
        return dateOfOpening;
    }
}
