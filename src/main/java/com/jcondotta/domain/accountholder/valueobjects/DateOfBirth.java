package com.jcondotta.domain.accountholder.valueobjects;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

import static com.jcondotta.domain.shared.ValidationErrors.AccountHolder;

public record DateOfBirth(LocalDate value) {

    private static final Clock SYSTEM_UTC_CLOCK = Clock.systemUTC();

    public DateOfBirth {
        Objects.requireNonNull(value, AccountHolder.DATE_OF_BIRTH_NOT_NULL);

        if (value.isAfter(LocalDate.now(SYSTEM_UTC_CLOCK))) {
            throw new IllegalArgumentException(AccountHolder.DATE_OF_BIRTH_NOT_IN_PAST);
        }
    }

    public static DateOfBirth of(LocalDate value) {
        return new DateOfBirth(value);
    }

    public int age(Clock clock) {
        return value.until(LocalDate.now(clock)).getYears();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
