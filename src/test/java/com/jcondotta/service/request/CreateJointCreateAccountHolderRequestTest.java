package com.jcondotta.service.request;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.jcondotta.service.request.ValidationTestHelper.assertSingleViolation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class CreateJointCreateAccountHolderRequestTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Test
    void shouldNotDetectConstraintViolation_whenRequestIsValid() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, jeffersonAccountHolderRequest);

        assertDoesNotThrow(() -> request.validateWith(VALIDATOR));
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new CreateAccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "accountHolder.accountHolderName.notBlank", "accountHolderRequest.accountHolderName");
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new CreateAccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "accountHolder.accountHolderName.tooLong", "accountHolderRequest.accountHolderName");
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "accountHolder.dateOfBirth.past", "accountHolderRequest.dateOfBirth");
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "accountHolder.dateOfBirth.past", "accountHolderRequest.dateOfBirth");
    }

    @Test
    void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "accountHolder.passportNumber.notNull", "accountHolderRequest.passportNumber");
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldDetectConstraintViolation_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validateWith(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "accountHolder.passportNumber.invalidLength", "accountHolderRequest.passportNumber");
    }

    @Test
    void shouldDetectMultipleConstraintViolation_whenAllFieldsAreNull() {
        var accountHolderRequest = new CreateAccountHolderRequest(null, null, null);
        var request = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

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