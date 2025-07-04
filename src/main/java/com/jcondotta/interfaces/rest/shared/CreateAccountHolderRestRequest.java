package com.jcondotta.interfaces.rest.shared;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static com.jcondotta.domain.shared.ValidationErrors.AccountHolder;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Request object used for creating a new account holder.")
public record CreateAccountHolderRestRequest(

    @Schema(description = "Name of the account holder", example = "Jefferson Condotta", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = AccountHolder.NAME_NOT_BLANK)
    @Size(max = AccountHolderName.MAX_LENGTH, message = AccountHolder.NAME_TOO_LONG)
    String accountHolderName,

    @Schema(description = "Date of birth of the account holder", example = "1990-11-23", pattern = "yyyy-MM-dd", requiredMode = RequiredMode.REQUIRED)
    @Past(message = AccountHolder.DATE_OF_BIRTH_NOT_IN_PAST)
    @NotNull(message = AccountHolder.DATE_OF_BIRTH_NOT_NULL)
    LocalDate dateOfBirth,

    @Schema(description = "Passport number of the account holder", example = "FH254787", requiredMode = RequiredMode.REQUIRED)
    @Size(min = PassportNumber.LENGTH, max = PassportNumber.LENGTH, message = AccountHolder.PASSPORT_NUMBER_INVALID_LENGTH)
    @NotNull(message = AccountHolder.PASSPORT_NUMBER_NOT_NULL)
    String passportNumber
) { }