package com.jcondotta.helper;

import com.jcondotta.service.request.AccountHolderRequest;

import java.time.LocalDate;
import java.time.Month;

public enum TestAccountHolderRequest {

    JEFFERSON("Jefferson Condotta", "FH254787", LocalDate.of(1988, Month.JUNE, 24)),
    VIRGINIO("Virginio Condotta", "BC858683", LocalDate.of(1917, Month.DECEMBER, 11)),
    PATRIZIO("Patrizio Condotta", "AA527570", LocalDate.of(1889, Month.FEBRUARY, 18));

    private final String accountHolderName;
    private final String passportNumber;
    private final LocalDate dateOfBirth;

    TestAccountHolderRequest(String accountHolderName, String passportNumber, LocalDate dateOfBirth) {
        this.accountHolderName = accountHolderName;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public AccountHolderRequest toAccountHolderRequest() {
        return new AccountHolderRequest(this.accountHolderName, this.dateOfBirth, this.passportNumber);
    }
}