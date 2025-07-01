package com.jcondotta.application.usecase.bankaccount;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountLookupUseCaseImpl implements BankAccountLookupUseCase {

    private final BankAccountLookupRepository bankAccountLookupRepository;
    private final CacheStore<BankAccount> cacheStore;

    @Override
    public BankAccount lookup(BankAccountId bankAccountId) {
        var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(bankAccountId);

        return cacheStore.getOrFetch(bankAccountIdCacheKey, () -> bankAccountLookupRepository.lookup(bankAccountId))
            .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));
    }
}