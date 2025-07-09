package com.jcondotta.domain.shared.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreatedAtTest {

    private static final ZoneId MADRID_ZONE = ZoneId.of("Europe/Madrid");
    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final Instant FIXED_INSTANT = Instant.parse("2025-07-05T12:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, MADRID_ZONE);

    @Test
    void shouldCreateNowWithZoneId() {
        var before = Instant.now();
        var createdAt = CreatedAt.now(MADRID_ZONE);
        var after = Instant.now();

        assertThat(createdAt.zoneId()).isEqualTo(MADRID_ZONE);
        assertThat(createdAt.value()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
    }

    @Test
    void shouldCreateNowWithClock() {
        var createdAt = CreatedAt.now(FIXED_CLOCK);

        assertThat(createdAt.value()).isEqualTo(FIXED_INSTANT);
        assertThat(createdAt.zoneId()).isEqualTo(MADRID_ZONE);
        assertThat(createdAt.asZonedDateTime().getZone()).isEqualTo(MADRID_ZONE);
    }

    @Test
    void shouldCreateFromInstantAndZone() {
        var createdAt = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);

        assertThat(createdAt.value()).isEqualTo(FIXED_INSTANT);
        assertThat(createdAt.zoneId()).isEqualTo(MADRID_ZONE);
    }

    @Test
    void shouldCreateFromZonedDateTime() {
        var zonedDateTime = ZonedDateTime.ofInstant(FIXED_INSTANT, MADRID_ZONE);
        var createdAt = CreatedAt.of(zonedDateTime);

        assertThat(createdAt.value()).isEqualTo(FIXED_INSTANT);
        assertThat(createdAt.zoneId()).isEqualTo(MADRID_ZONE);
    }

    @Test
    void shouldConvertToZonedDateTime() {
        var createdAt = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);

        var zoned = createdAt.asZonedDateTime();
        assertThat(zoned.getZone()).isEqualTo(MADRID_ZONE);
        assertThat(zoned.toInstant()).isEqualTo(FIXED_INSTANT);
    }

    @Test
    void shouldConvertToUTCZonedDateTime() {
        var createdAt = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);

        var utcZoned = createdAt.asUTCZonedDateTime();
        assertThat(utcZoned.getZone()).isEqualTo(UTC);
        assertThat(utcZoned.toInstant()).isEqualTo(FIXED_INSTANT);
    }

    @Test
    void shouldFormatValueAndZoneAsStrings() {
        var createdAt = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);

        assertThat(createdAt.valueAsString()).isEqualTo("2025-07-05T12:00:00Z");
        assertThat(createdAt.zoneIdAsString()).isEqualTo("Europe/Madrid");
    }

    @Test
    void shouldToStringAsZonedDateTimeFormat() {
        var createdAt = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);

        var expected = ZonedDateTime.ofInstant(FIXED_INSTANT, MADRID_ZONE).toString();
        assertThat(createdAt.toString()).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionIfInstantIsNull() {
        assertThatThrownBy(() -> new CreatedAt(null, MADRID_ZONE))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("value must not be null");
    }

    @Test
    void shouldThrowExceptionIfZoneIdIsNull() {
        assertThatThrownBy(() -> new CreatedAt(FIXED_INSTANT, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("zoneId must not be null");
    }

    @Test
    void shouldBeEqualWhenSameInstantAndZone() {
        var a = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
        var b = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentZoneId() {
        var a = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
        var b = CreatedAt.of(FIXED_INSTANT, UTC);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void shouldNotBeEqualWhenDifferentInstant() {
        var a = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
        var b = CreatedAt.of(FIXED_INSTANT.plusSeconds(1), MADRID_ZONE);

        assertThat(a).isNotEqualTo(b);
    }
}
