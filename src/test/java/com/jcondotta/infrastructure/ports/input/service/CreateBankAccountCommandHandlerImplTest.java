package com.jcondotta.infrastructure.ports.input.service;


import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.config.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import jakarta.validation.Validator;
import net.datafaker.Faker;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountCommandHandlerImplTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();
//    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    private final Faker faker = new Faker();

    @Mock
    private CreateBankAccountRepository createBankAccountRepository;

//    @Mock
//    private BankAccountIbanGeneratorService bankAccountIbanGeneratorService;

//    private CreateBankAccountCommandHandler createBankAccountCommandHandler;

//    @BeforeEach
//    void beforeEach() {
//        createBankAccountCommandHandler = new CreateBankAccountCommandHandlerImpl(
//            createBankAccountRepository,
//            TEST_CLOCK_FIXED_INSTANT,
//            VALIDATOR,
//            bankAccountIbanGeneratorService,
//            BANKING_ENTITY_MAPPER
//        );
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BankAccountTypeAndCurrencyArgumentsProvider.class)
//    void shouldCreateBankAccountBankAccount_whenRequestIsValid(AccountType accountType, Currency currency) {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(accountType)
//            .currency(currency)
//            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
//            .build();
//
//        var bankAccountCaptor = ArgumentCaptor.forClass(BankingEntity.class);
//        var accountHolderCaptor = ArgumentCaptor.forClass(BankingEntity.class);
//
//        when(bankAccountIbanGeneratorService.generateIban()).thenReturn(faker.finance().iban());
//        createBankAccountCommandHandler.handle(request);
//
//        verify(createBankAccountRepository).createBankAccount(bankAccountCaptor.capture(), accountHolderCaptor.capture());
//
//        assertThat(bankAccountCaptor.getValue())
//                .satisfies(bankAccountDetails -> assertAll(
//                        () -> assertThat(bankAccountDetails.getBankAccountId()).isNotNull(),
//                        () -> assertThat(bankAccountDetails.isEntityTypeBankAccount()).isTrue(),
//                        () -> assertThat(bankAccountDetails.getIban()).isNotNull(),
//                        () -> assertThat(bankAccountDetails.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT)),
//                        () -> assertThat(bankAccountDetails.getAccountHolderId()).isNull(),
//                        () -> assertThat(bankAccountDetails.getAccountHolderName()).isNull(),
//                        () -> assertThat(bankAccountDetails.getPassportNumber()).isNull(),
//                        () -> assertThat(bankAccountDetails.getDateOfBirth()).isNull()
//                ));
//
//        assertThat(accountHolderCaptor.getValue())
//                .satisfies(accountHolder -> assertAll(
//                        () -> assertThat(accountHolder.getAccountHolderId()).isNotNull(),
//                        () -> assertThat(accountHolder.getAccountHolderName()).isEqualTo(request.accountHolder().accountHolderName()),
//                        () -> assertThat(accountHolder.getPassportNumber()).isEqualTo(request.accountHolder().passportNumber()),
//                        () -> assertThat(accountHolder.getDateOfBirth()).isEqualTo(request.accountHolder().dateOfBirth()),
//                        () -> assertThat(accountHolder.getBankAccountId()).isEqualTo(bankAccountCaptor.getValue().getBankAccountId()),
//                        () -> assertThat(accountHolder.getIban()).isNull(),
//                        () -> assertThat(accountHolder.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT))
//                ));
//
//        verifyNoMoreInteractions(createBankAccountRepository);
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                blankAccountHolderName,
//                DATE_OF_BIRTH_JEFFERSON,
//                PASSPORT_NUMBER_JEFFERSON
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
//        final var veryLongAccountHolderName = "J".repeat(256);
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                veryLongAccountHolderName,
//                DATE_OF_BIRTH_JEFFERSON,
//                PASSPORT_NUMBER_JEFFERSON
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenDateOfBirthIsNull() {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                null,
//                PASSPORT_NUMBER_JEFFERSON
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
//        LocalDate futureDate = LocalDate.now().plusDays(1);
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                futureDate,
//                PASSPORT_NUMBER_JEFFERSON
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
//        LocalDate today = LocalDate.now();
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                today,
//                PASSPORT_NUMBER_JEFFERSON
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                DATE_OF_BIRTH_JEFFERSON,
//                null
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                DATE_OF_BIRTH_JEFFERSON,
//                invalidLengthPassportNumber
//            ))
//            .build();
//
//        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountCommandHandler.handle(request));
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//
//        verifyNoInteractions(createBankAccountRepository);
//    }
//
//    @Test
//    void shouldThrowDynamoDBException_whenRepositoryTransactionFails() {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
//            .build();
//
//        var exceptionMessage = "DynamoDB Transaction Failed";
//
//        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
//                .when(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());
//
//        var exception = assertThrows(DynamoDbException.class, () ->
//                createBankAccountCommandHandler.handle(request)
//        );
//
//        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
//        verify(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());
//
//        verifyNoMoreInteractions(createBankAccountRepository);
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"bankAccountDetails.notNull", "accountHolder.notNull"})
//    void shouldThrowNullPointerException_whenRepositoryThrowsNullPointerException(String exceptionMessage) {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
//            .build();
//
//        doThrow(new NullPointerException(exceptionMessage))
//                .when(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());
//
//        var exception = assertThrows(NullPointerException.class, () ->
//                createBankAccountCommandHandler.handle(request)
//        );
//
//        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
//        verify(createBankAccountRepository).createBankAccount(any(), (BankingEntity) any());
//
//        verifyNoMoreInteractions(createBankAccountRepository);
//    }
}