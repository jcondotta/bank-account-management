package com.jcondotta.service.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Serdeable
@Introspected
@Schema(description = "Request containing account holder information.")
public record AccountHolderRequest(

        @Schema(description = "Name of the account holder", example = "Jefferson Condotta", requiredMode = RequiredMode.REQUIRED)
        @NotBlank(message = "accountHolder.accountHolderName.notBlank")
        @Size(max = 255, message = "accountHolder.accountHolderName.tooLong")
        String accountHolderName,

        @Schema(description = "Date of birth of the account holder", example = "1990-11-23", pattern = "yyyy-MM-dd", requiredMode = RequiredMode.REQUIRED)
        @Past(message = "accountHolder.dateOfBirth.past")
        @NotNull(message = "accountHolder.dateOfBirth.notNull")
        LocalDate dateOfBirth,

        @Schema(description = "Passport number of the account holder", example = "FH254787", requiredMode = RequiredMode.REQUIRED)
        @Size(min = 8, max = 8, message = "accountHolder.passportNumber.invalidLength")
        @NotNull(message = "accountHolder.passportNumber.notNull")
        String passportNumber
)
{ }
