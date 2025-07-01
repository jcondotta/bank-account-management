package com.jcondotta.application.usecase.accountholder;

import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.application.ports.output.repository.CreateJointAccountHolderRepository;
import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import com.jcondotta.interfaces.rest.BankAccountMapper;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderRequest;
import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Objects;

@Component
@AllArgsConstructor
class CreateJointAccountHolderUseCaseImpl implements CreateJointAccountHolderUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderUseCaseImpl.class);

    private final UpdateBankAccountRepository updateBankAccountRepository;
    private final BankAccountLookupRepository bankAccountLookupRepository;
    private final CacheStore<BankAccount> cacheStore;
    private final BankAccountMapper bankingEntityMapper;
    private final Clock currentClock;

    @Override
    public BankAccount execute(BankAccountId bankAccountId, CreateJointAccountHolderRequest request) {
        LOGGER.atInfo()
            .setMessage("Initiating joint account holder creation process.")
            .log();

        AccountHolder jointAccountHolder = bankingEntityMapper
            .toJointAccountHolder(request.accountHolder(), currentClock);

        return bankAccountLookupRepository.lookup(bankAccountId)
            .map(bankAccount -> {
                bankAccount.addJointAccountHolder(jointAccountHolder);
                updateBankAccountRepository.update(bankAccount);

                var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(bankAccountId);
                cacheStore.put(bankAccountIdCacheKey, bankAccount);

                LOGGER.atDebug()
                    .setMessage("Joint account holder successfully added to bank account: {}")
                    .addArgument(bankAccountId.value())
                    .addKeyValue("bankAccountId", bankAccountId.value())
                    .log();

                return bankAccount;
            })
            .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));

    }
}
