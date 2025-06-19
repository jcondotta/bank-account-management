package com.jcondotta.infrastructure.ports.input.service;


import com.jcondotta.application.dto.AccountHolderDetailsRequest;
import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.application.ports.input.service.CreateBankAccountService;
import com.jcondotta.application.ports.input.service.BankAccountIbanGeneratorService;
import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.argument_provider.BankAccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.domain.model.AccountType;
import com.jcondotta.domain.model.BankingEntity;
import com.jcondotta.domain.model.Currency;
import com.jcondotta.helper.TestAccountHolderRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import net.datafaker.Faker;
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
class CreateBankAccountServiceImplTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();
    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    private final Faker faker = new Faker();

    @Mock
    private CreateBankAccountRepository createBankAccountRepository;

    @Mock
    private BankAccountIbanGeneratorService bankAccountIbanGeneratorService;

    private CreateBankAccountService createBankAccountService;

    @BeforeEach
    void beforeEach() {
        createBankAccountService = new CreateBankAccountServiceImpl(
            createBankAccountRepository,
            TEST_CLOCK_FIXED_INSTANT,
            VALIDATOR,
            bankAccountIbanGeneratorService,
            BANKING_ENTITY_MAPPER
        );
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountTypeAndCurrencyArgumentsProvider.class)
    void shouldCreateBankAccountBankAccount_whenRequestIsValid(AccountType accountType, Currency currency) {
        var request = CreateBankAccountRequest.builder()
            .accountType(accountType)
            .currency(currency)
            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
            .build();

        var bankAccountCaptor = ArgumentCaptor.forClass(BankingEntity.class);
        var accountHolderCaptor = ArgumentCaptor.forClass(BankingEntity.class);

        when(bankAccountIbanGeneratorService.generateIban()).thenReturn(faker.finance().iban());
        createBankAccountService.createBankAccount(request);

        verify(createBankAccountRepository).createBankAccount(bankAccountCaptor.capture(), accountHolderCaptor.capture());

        assertThat(bankAccountCaptor.getValue())
                .satisfies(bankAccount -> assertAll(
                        () -> assertThat(bankAccount.getBankAccountId()).isNotNull(),
                        () -> assertThat(bankAccount.isEntityTypeBankAccount()).isTrue(),
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
                        () -> assertThat(accountHolder.getAccountHolderName()).isEqualTo(request.accountHolder().accountHolderName()),
                        () -> assertThat(accountHolder.getPassportNumber()).isEqualTo(request.accountHolder().passportNumber()),
                        () -> assertThat(accountHolder.getDateOfBirth()).isEqualTo(request.accountHolder().dateOfBirth()),
                        () -> assertThat(accountHolder.getBankAccountId()).isEqualTo(bankAccountCaptor.getValue().getBankAccountId()),
                        () -> assertThat(accountHolder.getIban()).isNull(),
                        () -> assertThat(accountHolder.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT))
                ));

        verifyNoMoreInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                blankAccountHolderName,
                DATE_OF_BIRTH_JEFFERSON,
                PASSPORT_NUMBER_JEFFERSON
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                veryLongAccountHolderName,
                DATE_OF_BIRTH_JEFFERSON,
                PASSPORT_NUMBER_JEFFERSON
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsNull() {
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                null,
                PASSPORT_NUMBER_JEFFERSON
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                futureDate,
                PASSPORT_NUMBER_JEFFERSON
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                today,
                PASSPORT_NUMBER_JEFFERSON
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                DATE_OF_BIRTH_JEFFERSON,
                null
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(new AccountHolderDetailsRequest(
                ACCOUNT_HOLDER_NAME_JEFFERSON,
                DATE_OF_BIRTH_JEFFERSON,
                invalidLengthPassportNumber
            ))
            .build();

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.createBankAccount(request));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowDynamoDBException_whenRepositoryTransactionFails() {
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
            .build();

        var exceptionMessage = "DynamoDB Transaction Failed";

        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
                .when(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());

        var exception = assertThrows(DynamoDbException.class, () ->
                createBankAccountService.createBankAccount(request)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());

        verifyNoMoreInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"bankAccount.notNull", "accountHolder.notNull"})
    void shouldThrowNullPointerException_whenRepositoryThrowsNullPointerException(String exceptionMessage) {
        var request = CreateBankAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
            .build();

        doThrow(new NullPointerException(exceptionMessage))
                .when(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());

        var exception = assertThrows(NullPointerException.class, () ->
                createBankAccountService.createBankAccount(request)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());

        verifyNoMoreInteractions(createBankAccountRepository);
    }
}