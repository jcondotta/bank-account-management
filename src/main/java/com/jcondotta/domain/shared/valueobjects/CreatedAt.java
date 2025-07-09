package com.jcondotta.domain.shared.valueobjects;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public record CreatedAt(Instant value, ZoneId zoneId) {

    public CreatedAt {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
    }

    public static CreatedAt now(ZoneId zoneId) {
        return new CreatedAt(Instant.now(), zoneId);
    }

    public static CreatedAt now(Clock clock) {
        return new CreatedAt(clock.instant(), clock.getZone());
    }

    public static CreatedAt of(Instant instant, ZoneId zoneId) {
        return new CreatedAt(instant, zoneId);
    }

    public static CreatedAt of(ZonedDateTime zonedDateTime) {
        return new CreatedAt(zonedDateTime.toInstant(), zonedDateTime.getZone());
    }

    public ZonedDateTime asZonedDateTime() {
        return value.atZone(zoneId);
    }

    public ZonedDateTime asUTCZonedDateTime() {
        return value.atZone(ZoneId.of("UTC"));
    }

    public String valueAsString() {
        return value.toString(); // ex: "2025-07-05T12:00:00Z"
    }

    public String zoneIdAsString() {
        return zoneId.getId(); // ex: "Europe/Madrid"
    }

    @Override
    public String toString() {
        return asZonedDateTime().toString(); // ex: "2025-07-05T14:00+02:00[Europe/Madrid]"
    }
}