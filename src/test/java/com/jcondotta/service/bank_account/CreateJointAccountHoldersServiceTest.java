package com.jcondotta.service.bank_account;


import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.TestClockFactory;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.repository.CreateJointAccountHoldersRepository;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
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
import java.util.Arrays;
import java.util.List;
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
    private CreateJointAccountHoldersRepository repository;

    private CreateJointAccountHolderService createJointAccountHolderService;

    @SuppressWarnings("unchecked")
    private ArgumentCaptor<List<BankingEntity>> bankingEntityCaptor = ArgumentCaptor.forClass(List.class);

    @BeforeEach
    void beforeEach() {
        createJointAccountHolderService = new CreateJointAccountHolderService(repository, TEST_CLOCK_FIXED_INSTANT, VALIDATOR);
    }

    @Test
    void shouldCreateJointAccountHolder_whenRequestHasOneAccountHolder() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(jeffersonAccountHolderRequest);

        createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest);

        verify(repository).create(bankingEntityCaptor.capture());

        List<BankingEntity> capturedBankingEntities = bankingEntityCaptor.getValue();
        assertThat(capturedBankingEntities)
                .hasSize(1)
                .first()
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
    void shouldCreateJointAccountHolders_whenRequestHasMultipleAccountHolders() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var virginioAccountHolderRequest = TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest();
        var accountHolderRequests = List.of(jeffersonAccountHolderRequest, virginioAccountHolderRequest);

        createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, new CreateJointAccountHoldersRequest(accountHolderRequests));

        verify(repository).create(bankingEntityCaptor.capture());

        List<BankingEntity> capturedBankingEntities = bankingEntityCaptor.getValue();
        assertThat(capturedBankingEntities)
                .hasSize(2)
                .satisfies(entities -> assertAll(
                        () -> {
                            var jeffersonEntity = entities.get(0);
                            assertThat(jeffersonEntity.getAccountHolderId()).isNotNull();
                            assertThat(jeffersonEntity.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName());
                            assertThat(jeffersonEntity.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber());
                            assertThat(jeffersonEntity.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth());
                            assertThat(jeffersonEntity.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
                            assertThat(jeffersonEntity.getIban()).isNull();
                            assertThat(jeffersonEntity.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT));
                        },
                        () -> {
                            var virginioEntity = entities.get(1);
                            assertThat(virginioEntity.getAccountHolderId()).isNotNull();
                            assertThat(virginioEntity.getAccountHolderName()).isEqualTo(virginioAccountHolderRequest.accountHolderName());
                            assertThat(virginioEntity.getPassportNumber()).isEqualTo(virginioAccountHolderRequest.passportNumber());
                            assertThat(virginioEntity.getDateOfBirth()).isEqualTo(virginioAccountHolderRequest.dateOfBirth());
                            assertThat(virginioEntity.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
                            assertThat(virginioEntity.getIban()).isNull();
                            assertThat(virginioEntity.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT));
                        }
                ));
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHoldersListHasMoreThanTwoItems() {
        var accountHolderRequests = List.of(
                TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(),
                TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest(),
                TestAccountHolderRequest.PATRIZIO.toAccountHolderRequest()
        );

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, new CreateJointAccountHoldersRequest(accountHolderRequests))
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHoldersListIsEmpty() {
        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, new CreateJointAccountHoldersRequest(List.of()))
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderRequestsListContainsNullItem() {
        var accountHolderRequests = Arrays.asList(
                TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(),
                null
        );

        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequests);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }
}