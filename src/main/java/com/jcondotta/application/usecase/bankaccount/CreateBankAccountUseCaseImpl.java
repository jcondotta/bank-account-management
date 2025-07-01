package com.jcondotta.application.usecase.bankaccount;

import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.interfaces.rest.BankAccountMapper;
import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountRequest;
import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@AllArgsConstructor
class CreateBankAccountUseCaseImpl implements CreateBankAccountUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountUseCaseImpl.class);

    private final CreateBankAccountRepository createBankAccountRepository;
    private final CacheStore<BankAccount> cacheStore;
    private final BankAccountMapper bankAccountMapper;
    private final Clock currentClock;

    @Override
    public BankAccount execute(CreateBankAccountRequest request) {
        LOGGER.info("Initiating bank account and primary account holder creation process.");
        var bankAccount = bankAccountMapper.toBankAccount(request, currentClock);
        createBankAccountRepository.save(bankAccount);

        var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(bankAccount.bankAccountId());
        cacheStore.put(bankAccountIdCacheKey, bankAccount);

        LOGGER.debug("Bank account and primary account holder successfully created.");

        return bankAccount;
    }
}
