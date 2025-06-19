package com.jcondotta.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.domain.model.AccountHolderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "AccountHolderDetailsResponse", description = "Represents the details of an account holder.")
public class AccountHolderDetailsResponse {

    @NotNull
    UUID accountHolderId;

    @NotBlank
    @Schema(description = "Name of the account holder associated with this bank account.",
            example = "Jefferson Condotta",
            requiredMode = RequiredMode.REQUIRED)
    private String accountHolderName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of birth of the account holder.",
            example = "1988-02-01",
            requiredMode = RequiredMode.REQUIRED)
    private LocalDate dateOfBirth;

    @NotBlank
    @Schema(description = "Passport number of the account holder.",
            example = "FH254787",
            requiredMode = RequiredMode.REQUIRED)
    private String passportNumber;

    @NotNull
    @Schema(description = "Type of the account holder.",
            example = "PRIMARY", allowableValues = {"PRIMARY", "JOINT"},
            requiredMode = RequiredMode.REQUIRED)
    private AccountHolderType accountHolderType;

    @NotNull
    @Schema(description = "Date and time when the account holder was created.",
            example = "2023-08-23T14:55:00Z",
            requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime createdAt;

    @NotNull
    UUID bankAccountId;
}