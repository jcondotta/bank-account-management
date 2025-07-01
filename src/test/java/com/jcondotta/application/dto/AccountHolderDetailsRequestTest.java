package com.jcondotta.application.dto;

import com.jcondotta.interfaces.rest.AccountHolderDetailsRequest;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderRequest;
import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.jcondotta.service.request.ValidationTestHelper.assertSingleViolation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
class AccountHolderDetailsRequestTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Test
    void shouldNotDetectConstraintViolation_whenRequestIsValid() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();

        assertDoesNotThrow(() -> accountHolderRequest.validateWith(VALIDATOR));
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new AccountHolderDetailsRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            accountHolderRequest.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
            "accountHolder.accountHolderName.notBlank", "accountHolderName");
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderDetailsRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            accountHolderRequest.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
            "accountHolder.accountHolderName.tooLong", "accountHolderName");
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            accountHolderRequest.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
            "accountHolder.dateOfBirth.past", "dateOfBirth");
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            accountHolderRequest.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
            "accountHolder.dateOfBirth.past", "dateOfBirth");
    }

    @Test
    void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            accountHolderRequest.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
            "accountHolder.passportNumber.notNull", "passportNumber");
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldDetectConstraintViolation_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
            accountHolderRequest.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
            "accountHolder.passportNumber.invalidLength", "passportNumber");
    }

    @Test
    void shouldDetectMultipleConstraintViolation_whenAllFieldsAreNull() {
        var accountHolder = new AccountHolderDetailsRequest(null, null, null);
        var request = new CreateJointAccountHolderRequest(accountHolder);

        var expectedExceptionMessageKeys = List.of(
            "accountHolder.passportNumber.notNull",
            "accountHolder.accountHolderName.notBlank",
            "accountHolder.dateOfBirth.notNull"
        );

        var constraintViolations = VALIDATOR.validate(request);

        assertThat(constraintViolations)
            .hasSize(expectedExceptionMessageKeys.size())
            .extracting("message")
            .containsExactlyInAnyOrderElementsOf(expectedExceptionMessageKeys);
    }
}