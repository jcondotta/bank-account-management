package com.jcondotta.infrastructure.ports.input.service;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHolderCommandHandlerImplTest {

//    private static final BankAccountId BANK_ACCOUNT_ID_BRAZIL = new BankAccountId(
//        TestBankAccountId.BRAZIL.getBankAccountId());
//
//    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
//    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
//    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();
//
//    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;
//    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();
//
//    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;
//
//    @Mock
//    private CreateJointAccountHolderDynamoDBRepository repository;
//
//    private CreateJointAccountHolderCommandHandler createJointAccountHolderCommandHandler;
//
//    @BeforeEach
//    void beforeEach() {
//        createJointAccountHolderCommandHandler = new CreateJointAccountHolderCommandHandlerImpl(repository, TEST_CLOCK_FIXED_INSTANT, VALIDATOR, BANKING_ENTITY_MAPPER);
//    }
//
//    @Test
//    void shouldCreateJointAccountHolder_whenRequestIsValid() {
//        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(jeffersonAccountHolderRequest);
//
//        createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest);
//
//        var bankingEntityCaptor = ArgumentCaptor.forClass(BankingEntity.class);
//        verify(repository).save(bankingEntityCaptor.capture());
//
//        var capturedBankingEntity = bankingEntityCaptor.getValue();
//        assertThat(capturedBankingEntity)
//                .satisfies(bankingEntity -> assertAll(
//                        () -> assertThat(bankingEntity.getAccountHolderId()).isNotNull(),
//                        () -> assertThat(bankingEntity.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName()),
//                        () -> assertThat(bankingEntity.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber()),
//                        () -> assertThat(bankingEntity.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth()),
//                        () -> assertThat(bankingEntity.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL.value()),
//                        () -> assertThat(bankingEntity.getIban()).isNull(),
//                        () -> assertThat(bankingEntity.getCreatedAt()).isEqualTo(LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT))
//                ));
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenBankAccountIdIsNull() {
//        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(NullPointerException.class, () ->
//                createJointAccountHolderCommandHandler.handle(null, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getMessage()).isEqualTo("bankAccount.bankAccountId.notNull");
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
//        var accountHolderRequest = new AccountHolderDetailsRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
//        final var veryLongAccountHolderName = "J".repeat(256);
//        var accountHolderRequest = new AccountHolderDetailsRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
//        LocalDate futureDate = LocalDate.now().plusDays(1);
//        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
//        LocalDate today = LocalDate.now();
//        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
//        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
//        var accountHolderRequest = new AccountHolderDetailsRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(1);
//    }
//
//    @Test
//    void shouldThrowMultipleConstraintViolationException_whenAllFieldsAreNull() {
//        var accountHolderRequest = new AccountHolderDetailsRequest(null, null, null);
//        var createJointAccountHoldersRequest = new CreateJointAccountHolderRequest(accountHolderRequest);
//
//        var exception = assertThrows(ConstraintViolationException.class, () ->
//                createJointAccountHolderCommandHandler.handle(BANK_ACCOUNT_ID_BRAZIL, createJointAccountHoldersRequest)
//        );
//
//        assertThat(exception.getConstraintViolations()).hasSize(3);
//    }
}