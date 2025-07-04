package com.jcondotta.domain.shared.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.enums.Currency;

import java.util.Objects;

public record CurrencyValue(Currency value) {

    public CurrencyValue {
        Objects.requireNonNull(value, ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
    }

    public static CurrencyValue of(Currency value) {
        return new CurrencyValue(value);
    }

    public static CurrencyValue of(String currencyCode) {
        try {
            Objects.requireNonNull(currencyCode, ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
            return new CurrencyValue(Currency.valueOf(currencyCode.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ValidationErrors.BankAccount.CURRENCY_INVALID, e);
        }
    }

    public static CurrencyValue eur() {
        return new CurrencyValue(Currency.EUR);
    }

    public static CurrencyValue usd() {
        return new CurrencyValue(Currency.USD);
    }

    public boolean is(Currency currency) {
        return value == currency;
    }

    public boolean is(String code) {
        return value.name().equalsIgnoreCase(code);
    }

    public String symbol() {
        return value.symbol();
    }

    @Override
    public String toString() {
        return value.name();
    }
}
