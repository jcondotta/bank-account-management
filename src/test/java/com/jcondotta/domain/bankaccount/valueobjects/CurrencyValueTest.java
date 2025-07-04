package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.enums.Currency;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CurrencyValueTest {

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateCurrencyValue_whenUsingOfMethod(Currency currency) {
        var result = CurrencyValue.of(currency);

        assertThat(result)
            .isNotNull()
            .extracting(CurrencyValue::value)
            .isEqualTo(currency);
    }

    @ParameterizedTest
    @CsvSource({
        "eur, EUR",
        "usd, USD",
        "EUR, EUR",
        "USD, USD"
    })
    void shouldCreateCurrencyValue_whenUsingStringCode(String input, Currency expected) {
        var result = CurrencyValue.of(input);

        assertThat(result)
            .isNotNull()
            .extracting(CurrencyValue::value)
            .isEqualTo(expected);
    }

    @Test
    void shouldCreateEur_whenUsingStaticConstructor() {
        assertThat(CurrencyValue.eur())
            .isEqualTo(CurrencyValue.of(Currency.EUR));
    }

    @Test
    void shouldCreateUsd_whenUsingStaticConstructor() {
        assertThat(CurrencyValue.usd())
            .isEqualTo(CurrencyValue.of(Currency.USD));
    }

    @ParameterizedTest
    @CsvSource({
        "EUR, EUR, true",
        "USD, USD, true",
        "EUR, USD, false",
        "USD, EUR, false"
    })
    void shouldEvaluateCorrectly_whenCallingIsWithEnum(Currency source, Currency comparison, boolean expected) {
        var currencyValue = CurrencyValue.of(source);
        assertThat(currencyValue.is(comparison)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "EUR, eur, true",
        "USD, usd, true",
        "EUR, usd, false",
        "USD, eur, false"
    })
    void shouldEvaluateCorrectly_whenCallingIsWithString(Currency source, String comparison, boolean expected) {
        var currencyValue = CurrencyValue.of(source);
        assertThat(currencyValue.is(comparison)).isEqualTo(expected);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldReturnCorrectSymbol_whenCallingSymbol(Currency currency) {
        var value = CurrencyValue.of(currency);
        assertThat(value.symbol()).isEqualTo(currency.symbol());
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldReturnEnumName_whenCallingToString(Currency currency) {
        var value = CurrencyValue.of(currency);
        assertThat(value.toString()).isEqualTo(currency.name());
    }

    @Test
    void shouldThrowNullPointerException_whenCurrencyIsNull() {
        assertThatThrownBy(() -> CurrencyValue.of((Currency) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenCurrencyCodeIsNull() {
        assertThatThrownBy(() -> CurrencyValue.of((String) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenCurrencyCodeIsInvalid() {
        assertThatThrownBy(() -> CurrencyValue.of("BRL"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.BankAccount.CURRENCY_INVALID);
    }
}