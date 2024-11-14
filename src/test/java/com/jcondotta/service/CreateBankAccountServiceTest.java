package com.jcondotta.service;


import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.event.BankAccountCreatedSNSTopicPublisher;
import com.jcondotta.factory.TestClockFactory;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.repository.CreateBankAccountRepository;
import com.jcondotta.repository.CreateBankAccountResponse;
import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateBankAccountRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountServiceTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockFactory.testClockFixedInstant;
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Mock
    private CreateBankAccountRepository createBankAccountRepository;

    @Mock
    private CreateBankAccountResponse createBankAccountResponse;

    @Mock
    private BankAccountCreatedSNSTopicPublisher snsTopicPublisher;

    private CreateBankAccountService createBankAccountService;

    @BeforeEach
    void beforeEach() {
        createBankAccountService = new CreateBankAccountService(createBankAccountRepository, snsTopicPublisher, TEST_CLOCK_FIXED_INSTANT, VALIDATOR);
    }

    @Test
    void shouldCreateBankAccount_whenRequestIsValid() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        when(createBankAccountRepository.create(any(), any())).thenReturn(createBankAccountResponse);

        createBankAccountService.create(addBankAccountRequest);

        verify(createBankAccountRepository).create(any(), any());
        verify(snsTopicPublisher).publishMessage(any());

        verifyNoMoreInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowConstraintViolationException_whenRequestHasNullAccountHolder() {
        var addBankAccountRequest = new CreateBankAccountRequest(null);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsBlank(String blankAccountHolderName) {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowConstraintViolationException_whenAccountHolderNameIsLongerThan255Characters() {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowConstraintViolationException_whenDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowConstraintViolationException_whenPassportNumberIsNull() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exception = assertThrows(ConstraintViolationException.class, () -> createBankAccountService.create(addBankAccountRequest));
        assertThat(exception.getConstraintViolations()).hasSize(1);

        verifyNoInteractions(createBankAccountRepository, snsTopicPublisher);
    }

    @Test
    void shouldThrowDynamoDBException_whenRepositoryTransactionFails() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var exceptionMessage = "DynamoDB Transaction Failed";

        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
                .when(createBankAccountRepository).create(any(), any());

        var exception = assertThrows(DynamoDbException.class, () ->
                createBankAccountService.create(addBankAccountRequest)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(createBankAccountRepository).create(any(), any());

        verifyNoInteractions(snsTopicPublisher);
        verifyNoMoreInteractions(createBankAccountRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"bankAccount.notNull", "accountHolder.notNull"})
    void shouldThrowNullPointerException_whenRepositoryThrowsNullPointerException(String exceptionMessage) {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        doThrow(new NullPointerException(exceptionMessage))
                .when(createBankAccountRepository).create(any(), any());

        var exception = assertThrows(NullPointerException.class, () ->
                createBankAccountService.create(addBankAccountRequest)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(createBankAccountRepository).create(any(), any());

        verifyNoInteractions(snsTopicPublisher);
        verifyNoMoreInteractions(createBankAccountRepository);
    }

    @Test
    void shouldThrowException_whenSNSTopicPublisherFails() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        when(createBankAccountRepository.create(any(), any())).thenReturn(createBankAccountResponse);
        doThrow(new RuntimeException()).when(snsTopicPublisher).publishMessage(any());

        assertThrows(RuntimeException.class, () -> createBankAccountService.create(addBankAccountRequest));

        verify(createBankAccountRepository).create(any(), any());
        verify(snsTopicPublisher).publishMessage(any());

        verifyNoMoreInteractions(createBankAccountRepository, snsTopicPublisher);
    }
}