package com.jcondotta.domain.shared.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public record CreatedAt(LocalDateTime value) {

    private static final Clock SYSTEM_UTC_CLOCK = Clock.systemUTC();

    public CreatedAt {
        Objects.requireNonNull(value, ValidationErrors.CREATED_AT_NOT_NULL);

        if (value.isAfter(LocalDateTime.now(SYSTEM_UTC_CLOCK))) {
            throw new IllegalArgumentException(ValidationErrors.CREATED_AT_IN_FUTURE);
        }
    }

    public static CreatedAt of(LocalDateTime value) {
        return new CreatedAt(value);
    }

    public static CreatedAt now() {
        return new CreatedAt(LocalDateTime.now(SYSTEM_UTC_CLOCK));
    }

    public static CreatedAt now(Clock clock) {
        return new CreatedAt(LocalDateTime.now(clock));
    }

    public boolean isBefore(CreatedAt other) {
        return value.isBefore(other.value);
    }

    public boolean isAfter(CreatedAt other) {
        return value.isAfter(other.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
