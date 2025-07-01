package com.jcondotta.application.usecase.accountholder;

import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupRequest;
import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.AccountHolderLookupRepository;
import com.jcondotta.domain.accountholder.exceptions.AccountHolderNotFoundException;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountHolderLookupUseCaseImpl implements AccountHolderLookupUseCase {

    private final AccountHolderLookupRepository accountHolderLookupRepository;
    private final CacheStore<AccountHolder> cacheStore;

    @Override
    public AccountHolder lookup(AccountHolderLookupRequest accountHolderLookupRequest) {
        var bankAccountId = accountHolderLookupRequest.bankAccountId();
        var accountHolderId = accountHolderLookupRequest.accountHolderId();

        var accountHolderIdCacheKey = BankAccountCacheKey.accountHolderIdKey(bankAccountId, accountHolderId);
        return cacheStore.getOrFetch(accountHolderIdCacheKey, () -> accountHolderLookupRepository.lookup(accountHolderLookupRequest))
            .orElseThrow(() -> new AccountHolderNotFoundException(bankAccountId, accountHolderId));
    }
}