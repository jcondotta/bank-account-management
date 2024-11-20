package com.jcondotta.service.request;

import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import jakarta.validation.Validator;

import java.time.LocalDate;

public class CreateJointAccountHoldersRequestTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

//    @Test
//    void shouldNotDetectConstraintViolation_whenAccountHolderIsValid() {
//        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(jeffersonAccountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//        assertThat(constraintViolations).isEmpty();
//
//        assertThat(createJointAccountHolderRequest.accountHolderRequests())
//                .hasSize(1)
//                .extracting("accountHolderName")
//                .containsExactly(TestAccountHolderRequest.JEFFERSON.getAccountHolderName());
//    }
//
//    @Test
//    void shouldNotDetectConstraintViolation_whenAccountHoldersListHasTwoItems() {
//        var accountHolderRequests = List.of(
//                TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(),
//                TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest()
//        );
//
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequests);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//        assertThat(constraintViolations).isEmpty();
//
//        assertThat(createJointAccountHolderRequest.accountHolderRequests())
//                .hasSize(2)
//                .extracting("accountHolderName")
//                .containsExactlyInAnyOrder(
//                        TestAccountHolderRequest.JEFFERSON.getAccountHolderName(),
//                        TestAccountHolderRequest.VIRGINIO.getAccountHolderName()
//                );
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAccountHoldersListHasMoreThanTwoItems() {
//        var accountHolderRequests = List.of(
//                TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(),
//                TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest(),
//                TestAccountHolderRequest.PATRIZIO.toAccountHolderRequest()
//        );
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequests);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolders.tooMany"),
//                        () -> assertThat(violation.getPropertyPath().toString()).isEqualTo("accountHolderRequests")
//                ));
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAccountHoldersListIsEmpty() {
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(List.of());
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolders.notEmpty"),
//                        () -> assertThat(violation.getPropertyPath().toString()).isEqualTo("accountHolderRequests")
//                ));
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAccountHolderRequestsListContainsNullItem() {
//        List<AccountHolderRequest> accountHolderRequests = Arrays.asList(
//                TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(),
//                null
//        );
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequests);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.notNull"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[1].<list element>")
//                ));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
//        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.accountHolderName.notBlank"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].accountHolderName")
//                ));
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAccountHolderNameIsLongerThan255Characters() {
//        final var veryLongAccountHolderName = "J".repeat(256);
//        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.accountHolderName.tooLong"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].accountHolderName")
//                ));
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenDateOfBirthIsInFuture() {
//        LocalDate futureDate = LocalDate.now().plusDays(1);
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.dateOfBirth.past"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].dateOfBirth")
//                ));
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenDateOfBirthIsToday() {
//        LocalDate today = LocalDate.now();
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.dateOfBirth.past"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].dateOfBirth")
//                ));
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.passportNumber.notNull"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].passportNumber")
//                ));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
//    void shouldDetectConstraintViolation_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
//        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);
//
//        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> assertAll(
//                        () -> assertThat(violation.getMessage()).isEqualTo("accountHolder.passportNumber.invalidLength"),
//                        () -> assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].passportNumber")
//                ));
//    }
}
