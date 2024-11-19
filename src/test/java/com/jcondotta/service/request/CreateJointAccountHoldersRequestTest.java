package com.jcondotta.service.request;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateJointAccountHoldersRequestTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Test
    void shouldNotDetectConstraintViolation_whenAccountHoldersListHasOneItem() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);

        assertThat(constraintViolations).isEmpty();
        assertThat(createJointAccountHolderRequest.accountHolderRequests())
                .hasSize(1)
                .first()
                .isEqualTo(accountHolderRequest);
    }

    @Test
    void shouldNotDetectConstraintViolation_whenAccountHoldersListHasMultipleItems() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var virginioAccountHolderRequest = TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest();

        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, List.of(jeffersonAccountHolderRequest, virginioAccountHolderRequest)
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations).isEmpty();

        assertThat(createJointAccountHolderRequest.accountHolderRequests())
                .hasSize(2)
                .extracting("accountHolderName")
                .containsExactlyInAnyOrder(
                        TestAccountHolderRequest.JEFFERSON.getAccountHolderName(),
                        TestAccountHolderRequest.VIRGINIO.getAccountHolderName()
                );
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHoldersListHasMoreThan2Items() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var virginioAccountHolderRequest = TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest();
        var patrizioAccountHolderRequest = TestAccountHolderRequest.PATRIZIO.toAccountHolderRequest();
        var accountHolderRequests = List.of(jeffersonAccountHolderRequest, virginioAccountHolderRequest, patrizioAccountHolderRequest);

        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequests
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolders.tooMany");
                    assertThat(violation.getPropertyPath().toString()).isEqualTo("accountHolderRequests");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenBankAccountIdIsNull() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(null, List.of(accountHolderRequest));

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);

        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("bankAccount.bankAccountId.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("bankAccountId");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHoldersListIsEmpty() {
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, List.of()
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);

        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolders.notEmpty");
                    assertThat(violation.getPropertyPath().toString()).isEqualTo("accountHolderRequests");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHoldersListIsNull() {
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, (List<AccountHolderRequest>) null);

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);

        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolders.notEmpty");
                    assertThat(violation.getPropertyPath().toString()).isEqualTo("accountHolderRequests");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHolderRequestsListContainsNullItem() {
        List<AccountHolderRequest> accountHolderRequests = Arrays.asList(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(), null);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequests
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);

        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[1].<list element>");
                });
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.accountHolderName.notBlank");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].accountHolderName");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);

        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.accountHolderName.tooLong");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].accountHolderName");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.dateOfBirth.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].dateOfBirth");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.dateOfBirth.past");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].dateOfBirth");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.dateOfBirth.past");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].dateOfBirth");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.passportNumber.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].passportNumber");
                });
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldDetectConstraintViolation_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var constraintViolations = VALIDATOR.validate(createJointAccountHolderRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.passportNumber.invalidLength");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderRequests[0].passportNumber");
                });
    }
}
