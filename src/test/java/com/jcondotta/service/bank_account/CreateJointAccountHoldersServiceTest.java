package com.jcondotta.service.bank_account;


import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.repository.CreateJointAccountHolderRepository;
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

import java.time.LocalDate;
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

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Mock
    private CreateJointAccountHolderRepository repository;

    private CreateJointAccountHolderService createJointAccountHolderService;

    @BeforeEach
    void beforeEach() {
        createJointAccountHolderService = new CreateJointAccountHolderService(repository, VALIDATOR);
    }

    @Test
    void shouldCreateJointAccountHolder_whenRequestAccountHoldersListHasOneItem() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        createJointAccountHolderService.create(createJointAccountHoldersRequest);

        ArgumentCaptor<List<BankingEntity>> bankingEntityCaptor = ArgumentCaptor.forClass(List.class);
        verify(repository).create(bankingEntityCaptor.capture());

        List<BankingEntity> capturedBankingEntities = bankingEntityCaptor.getValue();
        assertThat(capturedBankingEntities)
                .hasSize(1)
                .first()
                .satisfies(bankingEntity -> {
                    assertThat(bankingEntity.getAccountHolderId()).isNotNull();
                    assertThat(bankingEntity.getAccountHolderName()).isEqualTo(accountHolderRequest.accountHolderName());
                    assertThat(bankingEntity.getPassportNumber()).isEqualTo(accountHolderRequest.passportNumber());
                    assertThat(bankingEntity.getDateOfBirth()).isEqualTo(accountHolderRequest.dateOfBirth());
                    assertThat(bankingEntity.getBankAccountId()).isEqualTo(createJointAccountHoldersRequest.bankAccountId());
                    assertThat(bankingEntity.getIban()).isNull();
                    assertThat(bankingEntity.getDateOfOpening()).isNull();
                });
    }

    @Test
    void shouldCreateJointAccountHolders_whenAccountHoldersListHasTwoItems() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var virginioAccountHolderRequest = TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest();
        var accountHolderRequests = List.of(jeffersonAccountHolderRequest, virginioAccountHolderRequest);

        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequests);

        createJointAccountHolderService.create(createJointAccountHoldersRequest);

        var bankingEntityCaptor = ArgumentCaptor.forClass(List.class);
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
                            assertThat(jeffersonEntity.getBankAccountId()).isEqualTo(createJointAccountHoldersRequest.bankAccountId());
                            assertThat(jeffersonEntity.getIban()).isNull();
                            assertThat(jeffersonEntity.getDateOfOpening()).isNull();
                        },
                        () -> {
                            var virginioEntity = entities.get(1);
                            assertThat(virginioEntity.getAccountHolderId()).isNotNull();
                            assertThat(virginioEntity.getAccountHolderName()).isEqualTo(virginioAccountHolderRequest.accountHolderName());
                            assertThat(virginioEntity.getPassportNumber()).isEqualTo(virginioAccountHolderRequest.passportNumber());
                            assertThat(virginioEntity.getDateOfBirth()).isEqualTo(virginioAccountHolderRequest.dateOfBirth());
                            assertThat(virginioEntity.getBankAccountId()).isEqualTo(createJointAccountHoldersRequest.bankAccountId());
                            assertThat(virginioEntity.getIban()).isNull();
                            assertThat(virginioEntity.getDateOfOpening()).isNull();
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
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequests);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenBankAccountIdIsNull() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(null, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHoldersListIsEmpty() {
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, List.of());

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderRequestsListContainsNullItem() {
        List<AccountHolderRequest> accountHolderRequests = Arrays.asList(
                TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest(),
                null
        );
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequests);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @Test
    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(
                BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest
        );

        var exception = assertThrows(ConstraintViolationException.class, () ->
                createJointAccountHolderService.create(createJointAccountHoldersRequest)
        );

        assertThat(exception.getConstraintViolations()).hasSize(1);
    }
}