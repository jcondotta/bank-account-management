package com.jcondotta.domain.shared.valueobjects;

import com.jcondotta.domain.shared.enums.Currency;
import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;

public record CurrencyValue(Currency value) {

    public CurrencyValue {
        Objects.requireNonNull(value, ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
    }

    public static CurrencyValue of(Currency value) {
        return new CurrencyValue(value);
    }
}
