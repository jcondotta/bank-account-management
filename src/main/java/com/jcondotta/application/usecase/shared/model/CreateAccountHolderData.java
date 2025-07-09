package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.shared.ValidationErrors;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public record CreateAccountHolderData(
    AccountHolderName accountHolderName,
    DateOfBirth dateOfBirth,
    PassportNumber passportNumber
) {

    public CreateAccountHolderData {
        requireNonNull(accountHolderName, ValidationErrors.AccountHolder.NAME_NOT_NULL);
        requireNonNull(dateOfBirth, ValidationErrors.AccountHolder.DATE_OF_BIRTH_NOT_NULL);
        requireNonNull(passportNumber, ValidationErrors.AccountHolder.PASSPORT_NUMBER_NOT_NULL);
    }

    public static CreateAccountHolderData of(String name, LocalDate dateOfBirth, String passportNumber) {
        return new CreateAccountHolderData(
            AccountHolderName.of(name),
            DateOfBirth.of(dateOfBirth),
            PassportNumber.of(passportNumber)
        );
    }
}
