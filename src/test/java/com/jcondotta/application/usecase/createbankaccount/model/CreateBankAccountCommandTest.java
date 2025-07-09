package com.jcondotta.application.usecase.createbankaccount.model;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateBankAccountCommandTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldCreateCommand_whenAllParametersAreValid(AccountType accountType, Currency currency) {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = new CreateBankAccountCommand(accountType, currency, accountHolderData);

        assertThat(command)
            .isNotNull()
            .satisfies(cmd -> {
                assertThat(cmd.accountType()).isEqualTo(accountType);
                assertThat(cmd.currency()).isEqualTo(currency);

                var holderData = cmd.createAccountHolderData();
                assertThat(holderData.accountHolderName().value()).isEqualTo(ACCOUNT_HOLDER_NAME);
                assertThat(holderData.dateOfBirth().value()).isEqualTo(DATE_OF_BIRTH);
                assertThat(holderData.passportNumber().value()).isEqualTo(PASSPORT_NUMBER);
            });
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldCreateCommand_whenUsingStaticFactoryMethod(AccountType accountType, Currency currency) {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = CreateBankAccountCommand.of(accountType, currency, accountHolderData);

        assertThat(command)
            .isNotNull()
            .satisfies(cmd -> {
                assertThat(cmd.accountType()).isEqualTo(accountType);
                assertThat(cmd.currency()).isEqualTo(currency);

                var holderData = cmd.createAccountHolderData();
                assertThat(holderData.accountHolderName().value()).isEqualTo(ACCOUNT_HOLDER_NAME);
                assertThat(holderData.dateOfBirth().value()).isEqualTo(DATE_OF_BIRTH);
                assertThat(holderData.passportNumber().value()).isEqualTo(PASSPORT_NUMBER);
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenAccountTypeIsNull(Currency currency) {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);

        assertThatThrownBy(() -> new CreateBankAccountCommand(null, currency, accountHolderData))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldThrowNullPointerException_whenCurrencyIsNull(AccountType accountType) {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);

        assertThatThrownBy(() -> new CreateBankAccountCommand(accountType, null, accountHolderData))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowNullPointerException_whenAccountHolderDataIsNull(AccountType accountType, Currency currency) {
        assertThatThrownBy(() -> new CreateBankAccountCommand(accountType, currency, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ACCOUNT_HOLDER_DATA_NOT_NULL);
    }
}
