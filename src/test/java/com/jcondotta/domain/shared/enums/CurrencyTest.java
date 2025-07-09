package com.jcondotta.domain.shared.enums;

import com.jcondotta.domain.bankaccount.enums.Currency;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTest {

    @Test
    void shouldRecognizeEuro() {
        Currency currency = Currency.EUR;

        assertThat(currency.isEuro()).isTrue();
        assertThat(currency.isUSDollar()).isFalse();
        assertThat(currency.isBritishPound()).isFalse();

        assertThat(currency.symbol()).isEqualTo("€");
        assertThat(currency.description()).isEqualTo("Euro");
    }

    @Test
    void shouldRecognizeUSDollar() {
        Currency currency = Currency.USD;

        assertThat(currency.isEuro()).isFalse();
        assertThat(currency.isUSDollar()).isTrue();
        assertThat(currency.isBritishPound()).isFalse();

        assertThat(currency.symbol()).isEqualTo("$");
        assertThat(currency.description()).isEqualTo("US Dollar");
    }

    @Test
    void shouldRecognizeBritishPound() {
        Currency currency = Currency.GBP;

        assertThat(currency.isEuro()).isFalse();
        assertThat(currency.isUSDollar()).isFalse();
        assertThat(currency.isBritishPound()).isTrue();

        assertThat(currency.symbol()).isEqualTo("£");
        assertThat(currency.description()).isEqualTo("British Pound");
    }
}