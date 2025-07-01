package com.jcondotta.domain.accountholder.valueobjects;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

import static com.jcondotta.domain.shared.ValidationErrors.AccountHolder;

public final class DateOfBirth {

    private final LocalDate value;

    @JsonCreator
    private DateOfBirth(@JsonProperty("value") LocalDate value) {
        this.value = value;
    }

    public static DateOfBirth of(LocalDate value, Clock clock) {
        Objects.requireNonNull(value, AccountHolder.DATE_OF_BIRTH_NOT_NULL);

        if (value.isAfter(LocalDate.now(clock))) {
            throw new IllegalArgumentException(AccountHolder.DATE_OF_BIRTH_IN_FUTURE);
        }

        return new DateOfBirth(value);
    }

    public LocalDate value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DateOfBirth other && Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}