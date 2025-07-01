package com.jcondotta.domain.value_object;

import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DateOfBirthTest {

    private static final Clock FIXED_CLOCK = TestClockConfig.testClockFixedInstant;

    private static final LocalDate VALID_DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);

    @Test
    void shouldCreateDateOfBirth_whenValueIsValid() {
        var dateOfBirth = DateOfBirth.of(VALID_DATE_OF_BIRTH, FIXED_CLOCK);

        assertThat(dateOfBirth)
            .isNotNull()
            .isInstanceOf(DateOfBirth.class)
            .extracting(DateOfBirth::value)
            .isEqualTo(VALID_DATE_OF_BIRTH);
    }

    @Test
    void shouldThrowNullPointerException_whenDateOfBirthIsNull() {
        assertThatThrownBy(() -> DateOfBirth.of(null, FIXED_CLOCK))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.DATE_OF_BIRTH_NOT_NULL);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenDateOfBirthIsInFuture() {
        var futureDate = LocalDate.of(2025, 1, 1);

        assertThatThrownBy(() -> DateOfBirth.of(futureDate, FIXED_CLOCK))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.AccountHolder.DATE_OF_BIRTH_IN_FUTURE);
    }

    @Test
    void shouldBeEqual_whenDateOfBirthsHaveSameValue() {
        var dob1 = DateOfBirth.of(VALID_DATE_OF_BIRTH, FIXED_CLOCK);
        var dob2 = DateOfBirth.of(VALID_DATE_OF_BIRTH, FIXED_CLOCK);

        assertThat(dob1)
            .isEqualTo(dob2)
            .hasSameHashCodeAs(dob2);
    }

    @Test
    void shouldNotBeEqual_whenDateOfBirthsHaveDifferentValues() {
        var dob1 = DateOfBirth.of(LocalDate.of(1990, 1, 1), FIXED_CLOCK);
        var dob2 = DateOfBirth.of(LocalDate.of(1995, 1, 1), FIXED_CLOCK);

        assertThat(dob1)
            .isNotEqualTo(dob2);
    }
}
