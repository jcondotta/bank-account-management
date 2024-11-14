package com.jcondotta.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;


@Serdeable
@Schema(name = "AccountHolderDTO", description = "Represents the details of an account holder.")
public class AccountHolderDTO {

    @NotNull
    @Schema(
            description = "Unique identifier for the account holder.",
            example = "114308a6-4903-4bfb-93a8-10ebebe23524",
            requiredMode = RequiredMode.REQUIRED
    )
    private UUID accountHolderId;

    @NotNull
    @Schema(
            description = "Unique identifier of the associated bank account.",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
            requiredMode = RequiredMode.REQUIRED
    )
    private UUID bankAccountId;

    @NotBlank
    @Schema(
            description = "Name of the account holder associated with this bank account.",
            example = "Jefferson Condotta",
            requiredMode = RequiredMode.REQUIRED
    )
    private String accountHolderName;

    @NotNull
    @Schema(
            description = "Date of birth of the account holder.",
            example = "1988-02-01",
            requiredMode = RequiredMode.REQUIRED
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank
    @Schema(
            description = "Passport number of the account holder.",
            example = "FH254787",
            requiredMode = RequiredMode.REQUIRED
    )
    private String passportNumber;

    @NotNull
    @Schema(
            description = "Type of the account holder (e.g., PRIMARY, JOINT).",
            example = "PRIMARY",
            requiredMode = RequiredMode.REQUIRED
    )
    private AccountHolderType accountHolderType;

    public AccountHolderDTO() {}

    public AccountHolderDTO(UUID bankAccountId,
                            UUID accountHolderId,
                            String accountHolderName,
                            LocalDate dateOfBirth,
                            String passportNumber,
                            AccountHolderType accountHolderType) {
        this.bankAccountId = bankAccountId;
        this.accountHolderId = accountHolderId;
        this.accountHolderName = accountHolderName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
        this.accountHolderType = accountHolderType;
    }

    public AccountHolderDTO(BankingEntity accountHolder) {
        this(
                accountHolder.getBankAccountId(),
                accountHolder.getAccountHolderId(),
                accountHolder.getAccountHolderName(),
                accountHolder.getDateOfBirth(),
                accountHolder.getPassportNumber(),
                accountHolder.getAccountHolderType()
        );
    }

    public UUID getAccountHolderId() {
        return accountHolderId;
    }
    public void setAccountHolderId(UUID accountHolderId) {
        this.accountHolderId = accountHolderId;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }
    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public AccountHolderType getAccountHolderType() {
        return accountHolderType;
    }
    public void setAccountHolderType(AccountHolderType accountHolderType) {
        this.accountHolderType = accountHolderType;
    }
}