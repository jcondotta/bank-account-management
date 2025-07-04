package com.jcondotta.domain.shared.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CreatedAtTest {

//    private static final Clock FIXED_CLOCK = Clock.fixed(
//        LocalDateTime.of(2024, 6, 30, 12, 0).toInstant(ZoneOffset.UTC),
//        ZoneId.of("UTC")
//    );
//
//    private static final LocalDateTime VALID_CREATED_AT = LocalDateTime.of(2024, 6, 30, 11, 59);
//
//    @Test
//    void shouldCreateCreatedAt_whenValueIsValid() {
//        var createdAt = CreatedAt.of(VALID_CREATED_AT, FIXED_CLOCK);
//
//        assertThat(createdAt)
//            .isNotNull()
//            .isInstanceOf(CreatedAt.class)
//            .extracting(CreatedAt::value)
//            .isEqualTo(VALID_CREATED_AT);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenCreatedAtIsNull() {
//        assertThatThrownBy(() -> CreatedAt.of(null, FIXED_CLOCK))
//            .isInstanceOf(NullPointerException.class)
//            .hasMessage(ValidationErrors.CREATED_AT_NOT_NULL);
//    }
//
//    @Test
//    void shouldThrowIllegalArgumentException_whenCreatedAtIsInFuture() {
//        var futureTime = LocalDateTime.of(2024, 6, 30, 12, 1);
//
//        assertThatThrownBy(() -> CreatedAt.of(futureTime, FIXED_CLOCK))
//            .isInstanceOf(IllegalArgumentException.class)
//            .hasMessage(ValidationErrors.CREATED_AT_IN_FUTURE);
//    }
//
//    @Test
//    void shouldCreateNow_whenClockIsProvided() {
//        var createdAt = CreatedAt.now(FIXED_CLOCK);
//
//        assertThat(createdAt.value())
//            .isEqualTo(LocalDateTime.of(2024, 6, 30, 12, 0));
//    }
//
//    @Test
//    void shouldBeEqual_whenCreatedAtValuesAreSame() {
//        var dateTime = LocalDateTime.of(2024, 6, 30, 11, 0);
//
//        var createdAt1 = CreatedAt.of(dateTime, FIXED_CLOCK);
//        var createdAt2 = CreatedAt.of(dateTime, FIXED_CLOCK);
//
//        assertThat(createdAt1)
//            .isEqualTo(createdAt2)
//            .hasSameHashCodeAs(createdAt2);
//    }
//
//    @Test
//    void shouldNotBeEqual_whenCreatedAtValuesAreDifferent() {
//        var createdAt1 = CreatedAt.of(LocalDateTime.of(2024, 6, 30, 10, 0), FIXED_CLOCK);
//        var createdAt2 = CreatedAt.of(LocalDateTime.of(2024, 6, 30, 11, 0), FIXED_CLOCK);
//
//        assertThat(createdAt1)
//            .isNotEqualTo(createdAt2);
//    }
}
