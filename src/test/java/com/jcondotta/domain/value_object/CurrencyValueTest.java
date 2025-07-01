package com.jcondotta.domain.value_object;

import com.jcondotta.domain.shared.enums.Currency;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CurrencyValueTest {

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateCurrencyValue_whenValueIsValid(Currency currency) {
        assertThat(CurrencyValue.of(currency))
            .isNotNull()
            .isInstanceOf(CurrencyValue.class)
            .extracting(CurrencyValue::value)
            .isEqualTo(currency);
    }

    @Test
    void shouldThrowNullPointerException_whenCurrencyValueIsNull() {
        assertThatThrownBy(() -> CurrencyValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldBeEqual_whenCurrencyValuesHaveSameValue(Currency currency) {
        var value1 = CurrencyValue.of(currency);
        var value2 = CurrencyValue.of(currency);

        assertThat(value1)
            .isEqualTo(value2)
            .hasSameHashCodeAs(value2);
    }

    @Test
    void shouldNotBeEqual_whenCurrencyValuesHaveDifferentValues() {
        var value1 = CurrencyValue.of(Currency.USD);
        var value2 = CurrencyValue.of(Currency.EUR);

        assertThat(value1)
            .isNotEqualTo(value2);
    }
}
