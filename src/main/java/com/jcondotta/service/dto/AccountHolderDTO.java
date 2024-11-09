package com.jcondotta.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Serdeable
@Schema(name = "AccountHolderDTO", description = "Represents an account holder details.")
public class AccountHolderDTO {

    @NotNull
    @Schema(description = "Name of the account holder associated with this bank account.", example = "Jefferson Condotta",
            requiredMode = RequiredMode.REQUIRED)
    private String accountHolderName;

    @NotNull
    @Schema(description = "Date of birth of the account holder.", example = "1988-02-01", requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank
    @Schema(description = "Passport number of the account holder.", example = "FH254787", requiredMode = RequiredMode.REQUIRED)
    private String passportNumber;

    public AccountHolderDTO(
            @JsonProperty("accountHolderName") String accountHolderName,
            @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
            @JsonProperty("passportNumber") String passportNumber) {
        this.accountHolderName = accountHolderName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
}
