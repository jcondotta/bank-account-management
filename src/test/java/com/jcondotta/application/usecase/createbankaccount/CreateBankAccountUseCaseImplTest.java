package com.jcondotta.application.usecase.createbankaccount;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.application.usecase.createbankaccount.mapper.CreateBankAccountResultMapper;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;
import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountUseCaseImplTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    @Mock
    private CreateBankAccountRepository repository;

    @Mock
    private CreateBankAccountResultMapper resultMapper;

    @Mock
    private CacheStore<BankAccount> cacheStore;

    @Captor
    private ArgumentCaptor<BankAccount> bankAccountCaptured;

    private CreateBankAccountUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateBankAccountUseCaseImpl(repository, resultMapper, cacheStore, FIXED_CLOCK);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldCreateBankAccountAndCacheIt_whenCommandIsValid(AccountType accountType, Currency currency) {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var createBankAccountCommand = CreateBankAccountCommand.of(accountType, currency, accountHolderData);

        useCase.execute(createBankAccountCommand);
        verify(repository).save(bankAccountCaptured.capture());

        var savedBankAccount = bankAccountCaptured.getValue();
        assertThat(savedBankAccount)
                .isNotNull()
                .satisfies(bankAccount -> {
                    assertThat(bankAccount.bankAccountId()).isNotNull();
                    assertThat(bankAccount.accountType()).isEqualTo(accountType);
                    assertThat(bankAccount.currency()).isEqualTo(currency);
                    assertThat(bankAccount.iban()).isNotNull();
                    assertThat(bankAccount.createdAt().value()).isEqualTo(Instant.now(FIXED_CLOCK));
                    assertThat(bankAccount.createdAt().zoneId()).isEqualTo(FIXED_CLOCK.getZone());
                    assertThat(bankAccount.accountHolders()).hasSize(1);
                    assertThat(bankAccount.getPrimaryAccountHolder())
                            .satisfies(accountHolder -> {
                                assertThat(accountHolder.accountHolderName().value()).isEqualTo(ACCOUNT_HOLDER_NAME);
                                assertThat(accountHolder.passportNumber().value()).isEqualTo(PASSPORT_NUMBER);
                                assertThat(accountHolder.dateOfBirth().value()).isEqualTo(DATE_OF_BIRTH);
                                assertThat(accountHolder.createdAt().value()).isEqualTo(Instant.now(FIXED_CLOCK));
                                assertThat(accountHolder.createdAt().zoneId()).isEqualTo(FIXED_CLOCK.getZone());
                            });;
                });

        verify(cacheStore).put(BankAccountCacheKey.bankAccountIdKey(savedBankAccount.bankAccountId()), savedBankAccount);
        verify(resultMapper).toResult(savedBankAccount);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldThrow_whenRepositoryFails(AccountType accountType, Currency currency) {
        var command = CreateBankAccountCommand.of(accountType, currency,
            CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER));

        doThrow(new RuntimeException("DB error")).when(repository).save(any());

        assertThatThrownBy(() -> useCase.execute(command))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("DB error");

        verify(repository).save(any());
        verifyNoInteractions(cacheStore, resultMapper);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldStillReturnResult_whenCacheFails(AccountType accountType, Currency currency) {
        var command = CreateBankAccountCommand.of(accountType, currency,
            CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER));

        var expectedResult = mock(CreateBankAccountResult.class);
        when(resultMapper.toResult(any())).thenReturn(expectedResult);
        doThrow(new RuntimeException("Cache down")).when(cacheStore).put(any(), any());

        useCase.execute(command);

        verify(repository).save(any());
        verify(cacheStore).put(any(), any());
        verify(resultMapper).toResult(any());
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldThrow_whenResultMapperFails(AccountType accountType, Currency currency) {
        var command = CreateBankAccountCommand.of(accountType, currency,
            CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER));

        when(resultMapper.toResult(any())).thenThrow(new IllegalStateException("mapping failed"));

        assertThatThrownBy(() -> useCase.execute(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("mapping failed");

        verify(repository).save(any());
        verify(cacheStore).put(any(), any());
        verify(resultMapper).toResult(any());
    }
}