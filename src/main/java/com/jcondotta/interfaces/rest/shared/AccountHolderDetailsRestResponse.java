package com.jcondotta.interfaces.rest.shared;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
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
@RequiredArgsConstructor
@Schema(name = "AccountHolderDetailsResponse", description = "Represents the details of an account holder.")
public class AccountHolderDetailsRestResponse {

    @NotNull
    @Schema(description = "The UUID value representing the account holder identifier.",
        example = "c6a4a1b2-0f8c-41e3-a622-98d66de824a9",
        requiredMode = Schema.RequiredMode.REQUIRED)
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
}