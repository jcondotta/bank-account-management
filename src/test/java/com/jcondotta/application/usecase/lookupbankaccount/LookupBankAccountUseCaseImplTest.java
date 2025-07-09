package com.jcondotta.application.usecase.lookupbankaccount;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.application.usecase.lookupbankaccount.mapper.LookupBankAccountResultMapper;
import com.jcondotta.application.usecase.lookupbankaccount.mapper.LookupBankAccountResultMapperImpl;
import com.jcondotta.application.usecase.lookupbankaccount.model.LookupBankAccountResult;
import com.jcondotta.application.usecase.shared.mapper.AccountHolderDetailsMapperImpl;
import com.jcondotta.application.usecase.shared.mapper.BankAccountDetailsMapperImpl;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.DomainErrorMessages;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LookupBankAccountUseCaseImplTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    @Mock
    private LookupBankAccountResult lookupBankAccountResultMock;

    @Mock
    private BankAccountLookupRepository lookupRepository;

    @Mock
    private LookupBankAccountResultMapper resultMapper;

    @Mock
    private CacheStore<BankAccount> cacheStore;

    private LookupBankAccountUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new LookupBankAccountUseCaseImpl(lookupRepository, resultMapper, cacheStore);
    }

    @Test
    void shouldReturnResult_whenAccountExistsInCacheOrRepository() {;
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME, FIXED_CLOCK);

        when(cacheStore.getOrFetch(any(), any())).thenReturn(Optional.of(bankAccount));
        when(resultMapper.toResult(bankAccount)).thenReturn(lookupBankAccountResultMock);

        LookupBankAccountResult bankAccountResult = useCase.lookup(bankAccount.bankAccountId());

        assertThat(bankAccountResult).isEqualTo(lookupBankAccountResultMock);
        verify(cacheStore).getOrFetch(any(), any());
        verify(resultMapper).toResult(bankAccount);
    }

    @Test
    void shouldReturnResult_whenCacheMissesButRepositoryReturnsBankAccount() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME, FIXED_CLOCK);

        when(lookupRepository.lookup(bankAccount.bankAccountId())).thenReturn(Optional.of(bankAccount));

        when(cacheStore.getOrFetch(eq(BankAccountCacheKey.of(bankAccount)), any()))
            .thenAnswer(invocation -> {
                var supplier = invocation.getArgument(1);
                return ((Supplier<Optional<BankAccount>>) supplier).get();
            });

        when(resultMapper.toResult(bankAccount)).thenReturn(lookupBankAccountResultMock);

        var result = useCase.lookup(bankAccount.bankAccountId());

        assertThat(result).isEqualTo(lookupBankAccountResultMock);

        verify(lookupRepository).lookup(bankAccount.bankAccountId());
        verify(resultMapper).toResult(bankAccount);
    }


    @Test
    void shouldThrowException_whenAccountNotFoundInCacheOrRepository() {
        var nonExistentBankAccountId = BankAccountId.newId();

        when(cacheStore.getOrFetch(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.lookup(nonExistentBankAccountId))
            .isInstanceOf(BankAccountNotFoundException.class)
            .hasMessage(DomainErrorMessages.BANK_ACCOUNT_NOT_FOUND_MESSAGE);

        verify(cacheStore).getOrFetch(eq(BankAccountCacheKey.bankAccountIdKey(nonExistentBankAccountId)), any());
        verifyNoInteractions(resultMapper);
    }
}
