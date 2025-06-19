package com.jcondotta;

import com.jcondotta.application.BankAccountLookupUseCase;
import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.application.ports.input.service.BankAccountLookupService;
import com.jcondotta.cache.BankAccountCacheKeys;
import com.jcondotta.cache.CacheStore;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.infrastructure.ports.input.service.BankAccountLookupServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountLookupUseCaseImpl implements BankAccountLookupUseCase {

    private final BankAccountLookupService bankAccountLookupService;

    public BankAccountLookupUseCaseImpl(BankAccountLookupService bankAccountLookupService) {
        this.bankAccountLookupService = bankAccountLookupService;
    }

//    @Override
//    public BankAccountLookupResponse lookupById(UUID bankAccountId) {
//        var bankAccountIdCacheKey = BankAccountCacheKeys.bankAccountIdKey(bankAccountId);
//
//        return cacheStore.getIfPresent(bankAccountIdCacheKey)
//            .orElseGet(() -> {
//                var bankAccountLookupByIdResponse = bankAccountLookupServiceImpl.lookup(new BankAccountId(bankAccountId));
//                cacheStore.put(bankAccountIdCacheKey, bankAccountLookupByIdResponse);
//                return bankAccountLookupByIdResponse;
//            });
//    }

    @Override
    public BankAccountLookupResponse lookup(BankAccountId bankAccountId) {
        return bankAccountLookupService.lookup(bankAccountId);
    }
}