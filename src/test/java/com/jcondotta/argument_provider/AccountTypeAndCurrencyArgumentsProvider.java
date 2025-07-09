package com.jcondotta.argument_provider;

import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.enums.Currency;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class AccountTypeAndCurrencyArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            Arguments.of(AccountType.CHECKING, Currency.EUR),
            Arguments.of(AccountType.CHECKING, Currency.USD),
            Arguments.of(AccountType.CHECKING, Currency.GBP),
            Arguments.of(AccountType.SAVINGS, Currency.EUR),
            Arguments.of(AccountType.SAVINGS, Currency.USD),
            Arguments.of(AccountType.SAVINGS, Currency.GBP)
        );
    }
}