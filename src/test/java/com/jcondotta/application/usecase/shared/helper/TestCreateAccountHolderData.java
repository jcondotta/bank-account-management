package com.jcondotta.application.usecase.shared.helper;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@AllArgsConstructor
public enum TestCreateAccountHolderData {

    JEFFERSON("Jefferson Condotta", "FH254787", LocalDate.of(1988, Month.JUNE, 24)),
    VIRGINIO("Virginio Condotta", "BC858683", LocalDate.of(1917, Month.DECEMBER, 11)),
    PATRIZIO("Patrizio Condotta", "AA527570", LocalDate.of(1889, Month.FEBRUARY, 18));

    private final String accountHolderName;
    private final String passportNumber;
    private final LocalDate dateOfBirth;

    public AccountHolderName getAccountHolderName() {
        return AccountHolderName.of(accountHolderName);
    }

    public DateOfBirth getDateOfBirth() {
        return DateOfBirth.of(dateOfBirth);
    }

    public PassportNumber getPassportNumber() {
        return PassportNumber.of(passportNumber);
    }

    public CreateAccountHolderData toCreateAccountHolderData() {
        return new CreateAccountHolderData(getAccountHolderName(), getDateOfBirth(), getPassportNumber());
    }
}