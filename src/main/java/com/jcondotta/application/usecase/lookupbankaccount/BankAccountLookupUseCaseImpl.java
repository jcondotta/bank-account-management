package com.jcondotta.application.usecase.lookupbankaccount;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.application.usecase.lookupbankaccount.mapper.BankAccountLookupResultMapper;
import com.jcondotta.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
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
    private final BankAccountLookupResultMapper resultMapper;
    private final CacheStore<BankAccount> cacheStore;

    @Override
    public BankAccountLookupResult lookup(BankAccountId bankAccountId) {
        var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(bankAccountId);

        return cacheStore.getOrFetch(bankAccountIdCacheKey, () -> bankAccountLookupRepository.lookup(bankAccountId))
            .map(resultMapper::toResult)
            .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));
    }
}