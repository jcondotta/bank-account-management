package com.jcondotta.helper;

import com.jcondotta.service.request.CreateAccountHolderRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Month;

@Getter
@AllArgsConstructor
public enum TestAccountHolderRequest {

    JEFFERSON("Jefferson Condotta", "FH254787", LocalDate.of(1988, Month.JUNE, 24)),
    VIRGINIO("Virginio Condotta", "BC858683", LocalDate.of(1917, Month.DECEMBER, 11)),
    PATRIZIO("Patrizio Condotta", "AA527570", LocalDate.of(1889, Month.FEBRUARY, 18));

    private final String accountHolderName;
    private final String passportNumber;
    private final LocalDate dateOfBirth;

    public CreateAccountHolderRequest toAccountHolderRequest() {
        return new CreateAccountHolderRequest(accountHolderName, dateOfBirth, passportNumber);
    }
}