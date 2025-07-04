package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class IbanTest {

    private static final String VALID_IBAN = "GB33BUKB20201555555555";

    @Test
    void shouldCreateIban_whenValueIsValid() {
        assertThat(Iban.of(VALID_IBAN))
            .isNotNull()
            .isInstanceOf(Iban.class)
            .extracting(Iban::value)
            .isEqualTo(VALID_IBAN);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowIllegalArgumentException_whenIbanIsBlank(String blankValue) {
        assertThatThrownBy(() -> Iban.of(blankValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.BankAccount.IBAN_NOT_BLANK);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenIbanFormatIsInvalid() {
        var invalidIban = "INVALID123456";

        assertThatThrownBy(() -> Iban.of(invalidIban))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.BankAccount.IBAN_INVALID_FORMAT);
    }

    @Test
    void shouldBeEqual_whenIbansHaveSameValue() {
        var iban1 = Iban.of(VALID_IBAN);
        var iban2 = Iban.of(VALID_IBAN);

        assertThat(iban1)
            .isEqualTo(iban2)
            .hasSameHashCodeAs(iban2);
    }

    @Test
    void shouldNotBeEqual_whenIbansHaveDifferentValues() {
        var iban1 = Iban.of(VALID_IBAN);
        var iban2 = Iban.of("GB94BARC10201530093459");

        assertThat(iban1)
            .isNotEqualTo(iban2);
    }
}
