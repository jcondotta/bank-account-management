package com.jcondotta.domain.shared.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcondotta.domain.shared.ValidationErrors;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public final class CreatedAt {

    private final LocalDateTime value;

    @JsonCreator
    private CreatedAt(@JsonProperty("value") LocalDateTime value) {
        this.value = value;
    }

    public static CreatedAt of(LocalDateTime value, Clock clock) {
        Objects.requireNonNull(value, ValidationErrors.CREATED_AT_NOT_NULL);

        var now = LocalDateTime.now(clock);
        if (value.isAfter(now)) {
            throw new IllegalArgumentException(ValidationErrors.CREATED_AT_IN_FUTURE);
        }

        return new CreatedAt(value);
    }

    public static CreatedAt now(Clock clock) {
        return new CreatedAt(LocalDateTime.now(clock));
    }

    public LocalDateTime value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CreatedAt other && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}