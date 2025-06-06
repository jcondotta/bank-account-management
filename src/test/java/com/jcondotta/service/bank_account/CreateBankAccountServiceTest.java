package com.jcondotta.service.bank_account;


import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.BankingEntityMapper;
import com.jcondotta.domain.EntityType;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.repository.CreateBankAccountRepository;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountServiceTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();
    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    @Mock
    private CreateBankAccountRepository createBankAccountRepository;

    private CreateBankAccountService createBankAccountService;

    @BeforeEach
    void beforeEach() {
        createBankAccountService = new CreateBankAccountService(createBankAccountRepository, TEST_CLOCK_FIXED_INSTANT, VALIDATOR, BANKING_ENTITY_MAPPER);
    }

    @Test
    void shouldCreateBankAccount_whenRequestIsValid() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();

        var bankAccountCaptor = ArgumentCaptor.forClass(BankingEntity.class);
        var accountHolderCaptor = ArgumentCaptor.forClass(BankingEntity.class);

        createBankAccountService.create(accountHolderRequest);
        verify(createBankAccountRepository).create(bankAccountCaptor.capture(), accountHolderCaptor.capture());

        assertThat(bankAccountCaptor.getValue())
                .satisfies(bankAccount -> assertAll(
                        () -> assertThat(bankAccount.getBankAccountId()).isNotNull(),
                        () -> assertThat(bankAccount.getEntityType()).isEqualTo(EntityType.BANK_ACCOUNT),
                        () -> assertThat(bankAccount.getIban()).isNotNull(),
                        () -> assertThat(bankAccount.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT)),
                        () -> assertThat(bankAccount.getAccountHolderId()).isNull(),
                        () -> assertThat(bankAccount.getAccountHolderName()).isNull(),
                        () -> assertThat(bankAccount.getPassportNumber()).isNull(),
                        () -> assertThat(bankAccount.getDateOfBirth()).isNull()
                ));

        assertThat(accountHolderCaptor.getValue())
                .satisfies(accountHolder -> assertAll(
                        () -> assertThat(accountHolder.getAccountHolderId()).isNotNull(),
                        () -> assertThat(accountHolder.getAccountHolderName()).isEqualTo(accountHolderRequest.accountHolderName()),
                        () -> assertThat(accountHolder.getPassportNumber()).isEqualTo(accountHolderRequest.passportNumber()),
                        () -> assertThat(accountHolder.getDateOfBirth()).isEqualTo(accountHolderRequest.dateOfBirth()),
                        () -> assertThat(accountHolder.getBankAccountId()).isEqualTo(bankAccountCaptor.getValue().getBankAccountId()),
                        () -> assertThat(accountHolder.getIban()).isNull(),
                        () -> assertThat(accountHolder.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT))
                ));

        verifyNoMoreInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new CreateAccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new CreateAccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsNull() {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(accountHolderRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowDynamoDBException_whenRepositoryTransactionFails() {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var exceptionMessage = "DynamoDB Transaction Failed";

        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
                .when(createBankAccountRepository).create(any(), any());

        var exception = assertThrows(DynamoDbException.class, () ->
                createBankAccountService.create(accountHolderRequest)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(createBankAccountRepository).create(any(), any());

        verifyNoMoreInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"bankAccount.notNull", "accountHolder.notNull"})
    void shouldThrowNullPointerException_whenRepositoryThrowsNullPointerException(String exceptionMessage) {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        doThrow(new NullPointerException(exceptionMessage))
                .when(createBankAccountRepository).create(any(), any());

        var exception = assertThrows(NullPointerException.class, () ->
                createBankAccountService.create(accountHolderRequest)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(createBankAccountRepository).create(any(), any());

        verifyNoMoreInteractions(createBankAccountRepository);
    }
}