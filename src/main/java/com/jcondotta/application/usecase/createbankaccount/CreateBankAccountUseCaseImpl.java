package com.jcondotta.application.usecase.createbankaccount;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.application.usecase.createbankaccount.mapper.CreateBankAccountCommandMapper;
import com.jcondotta.application.usecase.createbankaccount.mapper.CreateBankAccountResultMapper;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class CreateBankAccountUseCaseImpl implements CreateBankAccountUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountUseCaseImpl.class);

    private final CreateBankAccountRepository createBankAccountRepository;
    private final CreateBankAccountCommandMapper commandMapper;
    private final CreateBankAccountResultMapper resultMapper;
    private final CacheStore<BankAccount> cacheStore;

    @Override
    public CreateBankAccountResult execute(CreateBankAccountCommand command) {
        LOGGER.info("Initiating bank account and primary account holder creation process.");

        var bankAccount = commandMapper.toBankAccount(command);
        createBankAccountRepository.save(bankAccount);

        var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(bankAccount.bankAccountId());
        cacheStore.put(bankAccountIdCacheKey, bankAccount);

        return resultMapper.toResult(bankAccount);
    }
}
