package com.jcondotta.service.bank_account;

import com.jcondotta.domain.BankingEntityMapper;
import com.jcondotta.repository.CreateJointAccountHolderRepository;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.request.CreateJointAccountHolderRequest;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateJointAccountHolderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderService.class);

    private final CreateJointAccountHolderRepository repository;
    private final Clock currentClock;
    private final Validator validator;
    private final BankingEntityMapper bankingEntityMapper;

    public CreateJointAccountHolderService(CreateJointAccountHolderRepository repository, Clock currentClock, Validator validator, BankingEntityMapper bankingEntityMapper) {
        this.repository = repository;
        this.currentClock = currentClock;
        this.validator = validator;
        this.bankingEntityMapper = bankingEntityMapper;
    }

    public AccountHolderDTO create(CreateJointAccountHolderRequest request) {
        LOGGER.debug("Starting the creation process for a new account holder.");

        request.validateWith(validator);

        var jointAccountHolder = bankingEntityMapper.toJointAccountHolder(
                request.bankAccountId(),
                request.accountHolderRequest(),
                currentClock
        );

        repository.create(jointAccountHolder);

        var accountHolderDTO = bankingEntityMapper.toAccountHolderDto(jointAccountHolder);

        LOGGER.info("Account holder created successfully with ID: {}", jointAccountHolder.getPartitionKey());
        return accountHolderDTO;
    }
}
