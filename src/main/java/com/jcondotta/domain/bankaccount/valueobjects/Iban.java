package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

public record Iban(String value) {

    public Iban {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(ValidationErrors.BankAccount.IBAN_NOT_BLANK);
        }

        if (!IBANCheckDigit.IBAN_CHECK_DIGIT.isValid(value)) {
            throw new IllegalArgumentException(ValidationErrors.BankAccount.IBAN_INVALID_FORMAT);
        }
    }

    public static Iban of(String value) {
        return new Iban(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
