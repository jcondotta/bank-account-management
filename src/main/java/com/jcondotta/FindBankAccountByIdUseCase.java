package com.jcondotta;

import com.jcondotta.cache.CacheStore;
import com.jcondotta.service.bank_account.FindBankAccountService;
import com.jcondotta.service.dto.BankAccountDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class FindBankAccountByIdUseCase {

    private final FindBankAccountService findBankAccountService;

    @Qualifier("bankAccountCacheService")
    private final CacheStore<String, BankAccountDTO> bankAccountCacheService;

    public FindBankAccountByIdUseCase(FindBankAccountService findBankAccountService, CacheStore<String, BankAccountDTO> bankAccountCacheService) {
        this.findBankAccountService = findBankAccountService;
        this.bankAccountCacheService = bankAccountCacheService;
    }

    public BankAccountDTO findBankAccountById(UUID bankAccountId) {
        var cacheKey = bankAccountId.toString();
        return bankAccountCacheService.getIfPresent(cacheKey)
                .orElseGet(() -> {
                    BankAccountDTO bankAccountDTO = findBankAccountService.findBankAccountById(bankAccountId);
                    bankAccountCacheService.put(cacheKey, bankAccountDTO);
                    return bankAccountDTO;
                });
    }
}