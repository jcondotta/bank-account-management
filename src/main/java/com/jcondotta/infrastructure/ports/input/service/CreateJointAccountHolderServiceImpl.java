package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.application.dto.create.CreateJointAccountHolderRequest;
import com.jcondotta.application.dto.create.CreateJointAccountHolderResponse;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.application.ports.input.service.CreateJointAccountHolderService;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.infrastructure.ports.output.repository.CreateJointAccountHolderDynamoDBRepository;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Objects;
import java.util.UUID;

@Service
public class CreateJointAccountHolderServiceImpl implements CreateJointAccountHolderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderServiceImpl.class);

    private final CreateJointAccountHolderDynamoDBRepository repository;
    private final Clock currentClock;
    private final Validator validator;
    private final BankingEntityMapper bankingEntityMapper;

    public CreateJointAccountHolderServiceImpl(CreateJointAccountHolderDynamoDBRepository repository, Clock currentClock, Validator validator, BankingEntityMapper bankingEntityMapper) {
        this.repository = repository;
        this.currentClock = currentClock;
        this.validator = validator;
        this.bankingEntityMapper = bankingEntityMapper;
    }

    @Override
    public CreateJointAccountHolderResponse createJointAccountHolder(BankAccountId bankAccountId, CreateJointAccountHolderRequest request) {
        Objects.requireNonNull(bankAccountId, "bankAccount.bankAccountId.notNull");

        LOGGER.atInfo()
            .setMessage("Initiating joint account holder creation process.")
            .log();

        request.validateWith(validator);

        var accountHolderId = UUID.randomUUID();
        var jointAccountHolder = bankingEntityMapper.toJointAccountHolderEntity(accountHolderId, bankAccountId.value(), request, currentClock);

        repository.createJointAccountHolder(jointAccountHolder);

        LOGGER.atInfo()
            .setMessage("Joint account holder successfully created for bank account: {}")
            .addArgument(jointAccountHolder.getBankAccountId())
            .addKeyValue("bankAccountId", jointAccountHolder.getBankAccountId())
            .addKeyValue("accountHolderId", jointAccountHolder.getAccountHolderId())
            .log();

        return bankingEntityMapper.toCreateJointAccountHolderRequest(jointAccountHolder);
    }
}