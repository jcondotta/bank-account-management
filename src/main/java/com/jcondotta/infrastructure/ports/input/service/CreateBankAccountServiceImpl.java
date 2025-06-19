package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.application.ports.input.service.CreateBankAccountService;
import com.jcondotta.application.ports.input.service.BankAccountIbanGeneratorService;
import com.jcondotta.application.ports.output.repository.CreateBankAccountRepository;
import com.jcondotta.domain.model.BankAccountStatus;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class CreateBankAccountServiceImpl implements CreateBankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountServiceImpl.class);

    private final CreateBankAccountRepository createBankAccountRepository;
    private final Clock currentClock;
    private final Validator validator;
    private final BankAccountIbanGeneratorService bankAccountIbanGeneratorService;
    private final BankingEntityMapper bankingEntityMapper;

    public CreateBankAccountServiceImpl(CreateBankAccountRepository createBankAccountRepository,
                                        Clock currentClock,
                                        Validator validator,
                                        BankAccountIbanGeneratorService bankAccountIbanGeneratorService,
                                        BankingEntityMapper bankingEntityMapper) {
        this.createBankAccountRepository = createBankAccountRepository;
        this.currentClock = currentClock;
        this.validator = validator;
        this.bankAccountIbanGeneratorService = bankAccountIbanGeneratorService;
        this.bankingEntityMapper = bankingEntityMapper;
    }

    public CreateBankAccountResponse createBankAccount(@NotNull CreateBankAccountRequest request) {
        LOGGER.info("Initiating bank account and primary account holder creation process.");

        request.validateWith(validator);

        var bankAccountId = UUID.randomUUID();
        var bankAccountIban = bankAccountIbanGeneratorService.generateIban();
        var bankAccount = bankingEntityMapper.toBankAccountEntity(
            bankAccountId,
            request.accountType(),
            request.currency(),
            bankAccountIban,
            BankAccountStatus.PENDING,
            currentClock
        );

        var accountHolderId = UUID.randomUUID();
        var accountHolderRequest = request.accountHolder();
        var accountHolder = bankingEntityMapper.toPrimaryAccountHolderEntity(accountHolderId, bankAccountId, accountHolderRequest, currentClock);

        createBankAccountRepository.createBankAccount(bankAccount, accountHolder);

        LOGGER.debug("Bank account and primary account holder successfully created.");

        return bankingEntityMapper.toCreateBankAccountResponse(bankAccount, accountHolder);
    }
}