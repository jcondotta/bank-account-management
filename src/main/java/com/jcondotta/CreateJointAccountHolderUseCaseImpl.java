package com.jcondotta;

import com.jcondotta.application.CreateJointAccountHolderUseCase;
import com.jcondotta.application.dto.create.CreateJointAccountHolderRequest;
import com.jcondotta.application.dto.create.CreateJointAccountHolderResponse;
import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.application.ports.input.service.CreateJointAccountHolderService;
import com.jcondotta.cache.BankAccountCacheKeys;
import com.jcondotta.cache.CacheStore;
import com.jcondotta.domain.value_object.BankAccountId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateJointAccountHolderUseCaseImpl implements CreateJointAccountHolderUseCase {

    private final CreateJointAccountHolderService createJointAccountHolderService;
    private final CacheStore<String, BankAccountLookupResponse> cacheStore;

    public CreateJointAccountHolderUseCaseImpl(
            CreateJointAccountHolderService createJointAccountHolderService,
            CacheStore<String, BankAccountLookupResponse> cacheStore) {
        this.createJointAccountHolderService = createJointAccountHolderService;
        this.cacheStore = cacheStore;
    }

    @Override
    public CreateJointAccountHolderResponse createJointAccountHolder(BankAccountId bankAccountId, CreateJointAccountHolderRequest request) {
        var jointAccountHolderResponse = createJointAccountHolderService.createJointAccountHolder(bankAccountId, request);

        var bankAccountIdCacheKey = BankAccountCacheKeys.bankAccountIdKey(bankAccountId);
        cacheStore.evict(bankAccountIdCacheKey);

        return jointAccountHolderResponse;
    }
}
