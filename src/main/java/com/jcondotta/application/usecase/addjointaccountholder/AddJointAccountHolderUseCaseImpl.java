package com.jcondotta.application.usecase.addjointaccountholder;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.application.usecase.addjointaccountholder.mapper.AddJointAccountHolderCommandMapper;
import com.jcondotta.application.usecase.addjointaccountholder.mapper.AddJointAccountHolderResultMapper;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class AddJointAccountHolderUseCaseImpl implements AddJointAccountHolderUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddJointAccountHolderUseCaseImpl.class);

    private final UpdateBankAccountRepository updateBankAccountRepository;
    private final BankAccountLookupRepository bankAccountLookupRepository;
    private final CacheStore<BankAccount> cacheStore;
    private final AddJointAccountHolderCommandMapper commandMapper;
    private final AddJointAccountHolderResultMapper resultMapper;

    @Override
    public AddJointAccountHolderResult execute(AddJointAccountHolderCommand command) {
        LOGGER.atInfo()
            .setMessage("Initiating joint account holder creation process.")
            .log();

        return bankAccountLookupRepository.lookup(command.bankAccountId())
            .map(bankAccount -> {
                var jointAccountHolder = commandMapper.toJointAccountHolder(command.createAccountHolderData());

                bankAccount.addJointAccountHolder(jointAccountHolder);
                updateBankAccountRepository.update(bankAccount);

                var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(command.bankAccountId());
                cacheStore.put(bankAccountIdCacheKey, bankAccount);

                LOGGER.atDebug()
                    .setMessage("Joint account holder successfully added to bank account: {}")
                    .addArgument(command.bankAccountId().value())
                    .log();

                return resultMapper
                    .toResult(jointAccountHolder);

            })
            .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));
    }
}
