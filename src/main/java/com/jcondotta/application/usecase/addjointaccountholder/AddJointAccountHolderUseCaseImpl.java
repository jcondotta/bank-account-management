package com.jcondotta.application.usecase.addjointaccountholder;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.LookupBankAccountRepository;
import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.application.usecase.addjointaccountholder.mapper.AddJointAccountHolderResultMapper;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@AllArgsConstructor
class AddJointAccountHolderUseCaseImpl implements AddJointAccountHolderUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddJointAccountHolderUseCaseImpl.class);

    private final UpdateBankAccountRepository updateBankAccountRepository;
    private final LookupBankAccountRepository lookupBankAccountRepository;
    private final CacheStore<BankAccount> cacheStore;
    private final AddJointAccountHolderResultMapper resultMapper;
    private final Clock clock;

    @Override
    public AddJointAccountHolderResult execute(AddJointAccountHolderCommand command) {
        LOGGER.atInfo()
            .setMessage("Initiating joint account holder creation process.")
            .log();

        return lookupBankAccountRepository.lookup(command.bankAccountId())
            .map(bankAccount -> {
                var jointAccountHolder = AccountHolder.createJoint(command.createAccountHolderData(), clock);

                bankAccount.addJointAccountHolder(jointAccountHolder);
                updateBankAccountRepository.update(bankAccount);

                try {
                    cacheStore.put(BankAccountCacheKey.of(command.bankAccountId()), bankAccount);
                } catch (Exception ex) {
                    LOGGER.warn("Failed to cache bank account [{}]: {}", bankAccount.bankAccountId(), ex.getMessage());
                }

                LOGGER.atDebug()
                    .setMessage("Joint account holder successfully added to bank account: {}")
                    .addArgument(command.bankAccountId().value())
                    .log();

                return resultMapper.toResult(jointAccountHolder);

            })
            .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));
    }
}
