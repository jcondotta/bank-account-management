package com.jcondotta.service.bank_account;

import com.jcondotta.domain.BankingEntityMapper;
import com.jcondotta.repository.CreateBankAccountRepository;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateBankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountService.class);

    private final CreateBankAccountRepository createBankAccountRepository;
    private final Clock currentClock;
    private final Validator validator;
    private final BankingEntityMapper bankingEntityMapper;

    public CreateBankAccountService(CreateBankAccountRepository createBankAccountRepository,
                                    Clock currentClock, Validator validator, BankingEntityMapper bankingEntityMapper) {
        this.createBankAccountRepository = createBankAccountRepository;
        this.currentClock = currentClock;
        this.validator = validator;
        this.bankingEntityMapper = bankingEntityMapper;
    }

    public BankAccountDTO create(@NotNull CreateAccountHolderRequest createAccountHolderRequest) {
        LOGGER.debug("Starting the creation process for a new bank account and its primary account holder.");

        createAccountHolderRequest.validateWith(validator);

        var bankAccount = bankingEntityMapper.toBankAccount(currentClock);
        var accountHolder = bankingEntityMapper.toPrimaryAccountHolder(bankAccount.getBankAccountId(), createAccountHolderRequest, currentClock);

        LOGGER.info("Saving to DynamoDB: PK={}, SK={}, EntityType={}",
                accountHolder.getPartitionKey(), accountHolder.getSortKey(), accountHolder.getEntityType());

        createBankAccountRepository.create(bankAccount, accountHolder);
        var bankAccountDTO = bankingEntityMapper.toDto(bankAccount, accountHolder);

        LOGGER.debug("Bank account and primary account holder created successfully.");
        return bankAccountDTO;
    }
}
