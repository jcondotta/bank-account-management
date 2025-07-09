package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.application.usecase.shared.helper.TestCreateAccountHolderData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DateOfBirthTest {

    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestCreateAccountHolderData.JEFFERSON.getDateOfBirth().value();

    @Test
    void shouldCreateDateOfBirth_whenValueIsValid() {
        var dateOfBirth = DateOfBirth.of(DATE_OF_BIRTH_JEFFERSON);

        assertThat(dateOfBirth)
            .isNotNull()
            .isInstanceOf(DateOfBirth.class)
            .extracting(DateOfBirth::value)
            .isEqualTo(DATE_OF_BIRTH_JEFFERSON);
    }

    @Test
    void shouldThrowNullPointerException_whenDateOfBirthIsNull() {
        assertThatThrownBy(() -> DateOfBirth.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.DATE_OF_BIRTH_NOT_NULL);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenDateOfBirthIsInFuture() {
        var futureDate = LocalDate.now().plusDays(2);

        assertThatThrownBy(() -> DateOfBirth.of(futureDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.AccountHolder.DATE_OF_BIRTH_NOT_IN_PAST);
    }

    @Test
    void shouldBeEqual_whenDateOfBirthsHaveSameValue() {
        var dateOfBirth1 = DateOfBirth.of(DATE_OF_BIRTH_JEFFERSON);
        var dateOfBirth2 = DateOfBirth.of(DATE_OF_BIRTH_JEFFERSON);

        assertThat(dateOfBirth1)
            .isEqualTo(dateOfBirth2)
            .hasSameHashCodeAs(dateOfBirth2);
    }

    @Test
    void shouldNotBeEqual_whenDateOfBirthsHaveDifferentValues() {
        var dateOfBirth1 = DateOfBirth.of(LocalDate.of(1990, 1, 1));
        var dateOfBirth2 = DateOfBirth.of(LocalDate.of(1995, 1, 1));

        assertThat(dateOfBirth1)
            .isNotEqualTo(dateOfBirth2);
    }
}
