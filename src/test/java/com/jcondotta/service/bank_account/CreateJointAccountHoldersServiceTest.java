package com.jcondotta.service.bank_account;


import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.TestClockFactory;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.repository.CreateJointAccountHolderRepository;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHolderRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHoldersServiceTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockFactory.testClockFixedInstant;
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Mock
    private CreateJointAccountHolderRepository repository;

    private CreateJointAccountHolderService createJointAccountHolderService;

    @BeforeEach
    void beforeEach() {
        createJointAccountHolderService = new CreateJointAccountHolderService(repository, TEST_CLOCK_FIXED_INSTANT, VALIDATOR);
    }

    @Test
    void shouldCreateJointAccountHolder_whenRequestIsValid() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, jeffersonAccountHolderRequest);

        createJointAccountHolderService.create(createJointAccountHoldersRequest);

        var bankingEntityCaptor = ArgumentCaptor.forClass(BankingEntity.class);
        verify(repository).create(bankingEntityCaptor.capture());

        var capturedBankingEntity = bankingEntityCaptor.getValue();
        assertThat(capturedBankingEntity)
                .satisfies(bankingEntity -> assertAll(
                        () -> assertThat(bankingEntity.getAccountHolderId()).isNotNull(),
                        () -> assertThat(bankingEntity.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName()),
                        () -> assertThat(bankingEntity.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber()),
                        () -> assertThat(bankingEntity.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth()),
                        () -> assertThat(bankingEntity.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL),
                        () -> assertThat(bankingEntity.getIban()).isNull(),
                        () -> assertThat(bankingEntity.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT))
                ));
    }

    @Test
    void shouldThrowConstraintViolationException_whenBankAccountIdIsNull() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(null, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowMultipleConstraintViolationException_whenAllFieldsAreNull() {
        var accountHolderRequest = new AccountHolderRequest(null, null, null);
        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(3);
    }
}