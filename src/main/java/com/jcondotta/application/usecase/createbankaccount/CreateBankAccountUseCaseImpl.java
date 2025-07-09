package com.jcondotta.application.usecase.createbankaccount;

import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.application.usecase.createbankaccount.mapper.CreateBankAccountResultMapper;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
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
    private final CreateBankAccountResultMapper resultMapper;
    private final CacheStore<BankAccount> cacheStore;
    private final Clock clock;

    @Override
    public CreateBankAccountResult execute(CreateBankAccountCommand command) {
        LOGGER.info("Initiating bank account and primary account holder creation process.");

        var primaryAccountHolder = AccountHolder.createPrimary(command.createAccountHolderData(), clock);
        var bankAccount = BankAccount.openBankAccount(command.accountType(), command.currency(), Iban.generate(), primaryAccountHolder, clock);
        createBankAccountRepository.save(bankAccount);

        try {
            cacheStore.put(BankAccountCacheKey.of(bankAccount), bankAccount);
        } catch (Exception ex) {
            LOGGER.warn("Failed to cache bank account [{}]: {}", bankAccount.bankAccountId(), ex.getMessage());
        }

        return resultMapper.toResult(bankAccount);
    }
}
