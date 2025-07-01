package com.jcondotta.application.dto.create;

import com.jcondotta.interfaces.rest.AccountHolderDetailsRequest;
import com.jcondotta.argument_provider.BankAccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.enums.Currency;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountRequestTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @ParameterizedTest
    @ArgumentsSource(BankAccountTypeAndCurrencyArgumentsProvider.class)
    void shouldNotDetectConstraintViolation_whenRequestIsValid(AccountType accountType, Currency currency) {
        var accountHolderDetailsRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(accountType)
            .currency(currency)
            .accountHolder(accountHolderDetailsRequest)
            .build();

        assertDoesNotThrow(() -> createBankAccountRequest.validateWith(VALIDATOR));
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldDetectConstraintViolation_whenBankAccountTypeIsNull(Currency currency) {
        var accountHolderDetailsRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(null)
            .currency(currency)
            .accountHolder(accountHolderDetailsRequest)
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldDetectConstraintViolation_whenCurrencyIsNull(AccountType accountType) {
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(accountType)
            .currency(null)
            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.USD)
            .accountHolder(new AccountHolderDetailsRequest(
                blankAccountHolderName,
                DATE_OF_BIRTH_JEFFERSON,
                PASSPORT_NUMBER_JEFFERSON)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderDetailsRequest = new AccountHolderDetailsRequest(
            veryLongAccountHolderName,
            DATE_OF_BIRTH_JEFFERSON,
            PASSPORT_NUMBER_JEFFERSON);

        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                veryLongAccountHolderName,
                DATE_OF_BIRTH_JEFFERSON,
                PASSPORT_NUMBER_JEFFERSON)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsNull() {
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                null,
                PASSPORT_NUMBER_JEFFERSON)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                futureDate,
                PASSPORT_NUMBER_JEFFERSON)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                today,
                PASSPORT_NUMBER_JEFFERSON)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                DATE_OF_BIRTH_JEFFERSON,
                null)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldDetectConstraintViolation_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                DATE_OF_BIRTH_JEFFERSON,
                invalidLengthPassportNumber)
            )
            .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            createBankAccountRequest.validateWith(VALIDATOR)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountTypeAndCurrencyArgumentsProvider.class)
    void shouldDetectMultipleConstraintViolation_whenAllFieldsAreNull(AccountType accountType, Currency currency) {
        var createBankAccountRequest = CreateBankAccountRequest.builder()
            .accountType(accountType)
            .currency(currency)
            .accountHolder(new AccountHolderDetailsRequest(
                null,
                null,
                null)
            )
            .build();

        var constraintViolations = VALIDATOR.validate(createBankAccountRequest);

        assertThat(constraintViolations).hasSize(3);
    }
}