package com.jcondotta.application.usecase.addjointaccountholder;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.application.usecase.addjointaccountholder.mapper.AddJointAccountHolderResultMapper;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;
import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.DomainErrorMessages;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddJointAccountHolderUseCaseImplTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;
    private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();

    @Mock
    private UpdateBankAccountRepository updateRepository;

    @Mock
    private BankAccountLookupRepository lookupRepository;

    @Mock
    private CacheStore<BankAccount> cacheStore;

    @Mock
    private AddJointAccountHolderResultMapper resultMapper;

    @Mock
    private AddJointAccountHolderResult addJointAccountHolderResultMock;

    private AddJointAccountHolderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddJointAccountHolderUseCaseImpl(updateRepository, lookupRepository, cacheStore, resultMapper, FIXED_CLOCK);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldAddJointAccountHolder_whenBankAccountExists(AccountType accountType, Currency currency) {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = AddJointAccountHolderCommand.of(BANK_ACCOUNT_ID, accountHolderData);

        var primaryAccountHolderName = "Primary Account Holder";
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(
            accountType, currency, primaryAccountHolderName, FIXED_CLOCK
        );

        doReturn(Optional.of(bankAccount)).when(lookupRepository).lookup(BANK_ACCOUNT_ID);
        doReturn(addJointAccountHolderResultMock).when(resultMapper).toResult(any());

        useCase.execute(command);

        assertThat(bankAccount.jointAccountHolders())
            .hasSize(1)
            .first()
                .satisfies(accountHolder -> {
                    assertThat(accountHolder.accountHolderId().value()).isNotNull();
                    assertThat(accountHolder.accountHolderName().value()).isEqualTo(ACCOUNT_HOLDER_NAME);
                    assertThat(accountHolder.dateOfBirth().value()).isEqualTo(DATE_OF_BIRTH);
                    assertThat(accountHolder.passportNumber().value()).isEqualTo(PASSPORT_NUMBER);
                    assertThat(accountHolder.createdAt().value()).isEqualTo(Instant.now(FIXED_CLOCK));
                    assertThat(accountHolder.createdAt().zoneId()).isEqualTo(FIXED_CLOCK.getZone());
                });

        verify(updateRepository).update(bankAccount);
        verify(cacheStore).put(BankAccountCacheKey.of(BANK_ACCOUNT_ID), bankAccount);
        verify(resultMapper).toResult(any());
    }

    @Test
    void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = AddJointAccountHolderCommand.of(BANK_ACCOUNT_ID, accountHolderData);

        when(lookupRepository.lookup(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(command))
            .isInstanceOf(BankAccountNotFoundException.class)
            .hasMessageContaining(DomainErrorMessages.BANK_ACCOUNT_NOT_FOUND_MESSAGE);

        verifyNoInteractions(updateRepository, cacheStore, resultMapper);
    }

    @Test
    void shouldStillReturnResult_whenCacheFails() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = AddJointAccountHolderCommand.of(BANK_ACCOUNT_ID, accountHolderData);

        var primaryAccountHolderName = "Primary Account Holder";
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(primaryAccountHolderName, FIXED_CLOCK);

        when(lookupRepository.lookup(BANK_ACCOUNT_ID)).thenReturn(Optional.of(bankAccount));
        when(resultMapper.toResult(any())).thenReturn(addJointAccountHolderResultMock);
        doThrow(new RuntimeException("Cache failure")).when(cacheStore).put(any(), any());

        useCase.execute(command);

        verify(updateRepository).update(bankAccount);
        verify(cacheStore).put(BankAccountCacheKey.of(BANK_ACCOUNT_ID), bankAccount);
        verify(resultMapper).toResult(any());
    }

    @Test
    void shouldThrow_whenResultMapperFails() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = AddJointAccountHolderCommand.of(BANK_ACCOUNT_ID, accountHolderData);

        var primaryAccountHolderName = "Primary Account Holder";
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(primaryAccountHolderName, FIXED_CLOCK);

        when(lookupRepository.lookup(BANK_ACCOUNT_ID)).thenReturn(Optional.of(bankAccount));
        when(resultMapper.toResult(any())).thenThrow(new IllegalStateException("mapper failure"));

        assertThatThrownBy(() -> useCase.execute(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("mapper failure");

        verify(updateRepository).update(bankAccount);
        verify(cacheStore).put(any(), any());
        verify(resultMapper).toResult(any());
    }
}
