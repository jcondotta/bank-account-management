package com.jcondotta.application.dto.create;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.interfaces.rest.addjointaccountholder.model.AddJointAccountHolderRestRequest;
import com.jcondotta.interfaces.rest.shared.CreateAccountHolderRestRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class AddJointAccountHolderCommandTest {

//    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
//    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
//    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();
//
//    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();
//
//    @Test
//    void shouldNotDetectConstraintViolation_whenRequestIsValid() {
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
//        );
//        assertDoesNotThrow(() -> request.validateWith(VALIDATOR));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
//        );
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validateWith(VALIDATOR)
//        );
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAccountHolderNameIsLongerThan255Characters() {
//        final var veryLongAccountHolderName = "J".repeat(256);
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
//        );
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validateWith(VALIDATOR)
//        );
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenDateOfBirthIsInFuture() {
//        LocalDate futureDate = LocalDate.now().plusDays(1);
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON)
//        );
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validateWith(VALIDATOR)
//        );
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenDateOfBirthIsToday() {
//        LocalDate today = LocalDate.now();
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON)
//        );
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validateWith(VALIDATOR)
//        );
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null)
//        );
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validateWith(VALIDATOR)
//        );
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
//    void shouldDetectConstraintViolation_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber)
//        );
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validateWith(VALIDATOR)
//        );
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldDetectMultipleConstraintViolation_whenAllFieldsAreNull() {
//        var request = new AddJointAccountHolderRestRequest(
//            new CreateAccountHolderRestRequest(null, null, null)
//        );
//        var constraintViolations = VALIDATOR.validate(request);
//        assertThat(constraintViolations).hasSize(3);
//    }
}