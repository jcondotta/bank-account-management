package com.jcondotta.application.usecase.removejointaccountholder;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.BankAccountLookupRepository;
import com.jcondotta.application.ports.output.repository.UpdateBankAccountRepository;
import com.jcondotta.application.usecase.removejointaccountholder.model.RemoveJointAccountHolderCommand;
import com.jcondotta.domain.accountholder.exceptions.AccountHolderNotFoundException;
import com.jcondotta.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.BankAccountCacheKey;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class RemoveJointAccountHolderUseCaseImpl implements RemoveJointAccountHolderUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveJointAccountHolderUseCaseImpl.class);

    private final UpdateBankAccountRepository updateBankAccountRepository;
    private final BankAccountLookupRepository bankAccountLookupRepository;
    private final CacheStore<BankAccount> cacheStore;

    @Override
    public void execute(RemoveJointAccountHolderCommand command) {
        LOGGER.atInfo()
            .setMessage("Starting removal of joint account holder '{}' from bank account '{}'.")
            .addArgument(command.accountHolderId().value())
            .addArgument(command.bankAccountId().value())
            .log();

        bankAccountLookupRepository.lookup(command.bankAccountId())
            .ifPresentOrElse(bankAccount -> {
                var accountHolder = bankAccount.findAccountHolder(command.accountHolderId())
                    .orElseThrow(() -> new AccountHolderNotFoundException(command.bankAccountId(), command.accountHolderId()));

                bankAccount.removeJointAccountHolder(accountHolder);
                updateBankAccountRepository.update(bankAccount);

                var bankAccountIdCacheKey = BankAccountCacheKey.bankAccountIdKey(command.bankAccountId());
                cacheStore.put(bankAccountIdCacheKey, bankAccount);

                LOGGER.atInfo()
                    .setMessage("Joint account holder '{}' successfully removed from bank account '{}'.")
                    .addArgument(command.accountHolderId().value())
                    .addArgument(command.bankAccountId().value())
                    .log();

            }, () -> {
                throw new BankAccountNotFoundException(command.bankAccountId());
            });
    }
}
