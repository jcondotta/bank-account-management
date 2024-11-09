package com.jcondotta.helper;

import java.time.LocalDate;
import java.time.Month;

public enum TestAccountHolderFactory {

    JEFFERSON("Jefferson Condotta", "FH254787", LocalDate.of(1988, Month.JUNE, 24)),
    VIRGINIO("Virginio Condotta", "BC858683", LocalDate.of(1917, Month.DECEMBER, 11)),
    PATRIZIO("Patrizio Condotta", "AA5275702", LocalDate.of(1917, Month.FEBRUARY, 18));

    private final String accountHolderName;
    private final String passportNumber;
    private final LocalDate dateOfBirth;

    TestAccountHolderFactory(String accountHolderName, String passportNumber, LocalDate dateOfBirth) {
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
}