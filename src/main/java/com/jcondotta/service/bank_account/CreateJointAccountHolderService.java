package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.repository.CreateJointAccountHoldersRepository;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.AccountHoldersDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class CreateJointAccountHolderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderService.class);

    private final CreateJointAccountHoldersRepository repository;
    private final Clock currentInstant;
    private final Validator validator;

    @Inject
    public CreateJointAccountHolderService(CreateJointAccountHoldersRepository repository, Clock currentInstant, Validator validator) {
        this.repository = repository;
        this.currentInstant = currentInstant;
        this.validator = validator;
    }

    public AccountHoldersDTO create(@NotNull UUID bankAccountId, CreateJointAccountHoldersRequest createJointAccountHoldersRequest) {
        LOGGER.debug("Starting the creation process for a new account holders.");

        var constraintViolations = validator.validate(createJointAccountHoldersRequest);
        if (!constraintViolations.isEmpty()) {
            LOGGER.warn("Validation errors: {}", constraintViolations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", ")));

            throw new ConstraintViolationException(constraintViolations);
        }

        var jointAccountHolders = createJointAccountHoldersRequest.accountHolderRequests()
                .stream()
                .map(accountHolderRequest -> buildJointAccountHolder(bankAccountId, accountHolderRequest))
                .toList();

        repository.create(jointAccountHolders);

        return new AccountHoldersDTO(jointAccountHolders.stream()
                .map(AccountHolderDTO::new)
                .toList());
    }

    private BankingEntity buildJointAccountHolder(UUID bankAccountId, AccountHolderRequest accountHolderRequest) {
            return BankingEntity.buildAccountHolder(
                    UUID.randomUUID(),
                    accountHolderRequest.accountHolderName(),
                    accountHolderRequest.passportNumber(),
                    accountHolderRequest.dateOfBirth(),
                    AccountHolderType.JOINT,
                    LocalDateTime.now(currentInstant),
                    bankAccountId
                );
    }
}
