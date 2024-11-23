package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.event.AccountHolderCreatedSNSTopicPublisher;
import com.jcondotta.repository.CreateJointAccountHolderRepository;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHolderRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class CreateJointAccountHolderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderService.class);

    private final CreateJointAccountHolderRepository repository;
    private final AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;
    private final Clock currentInstant;
    private final Validator validator;

    @Inject
    public CreateJointAccountHolderService(CreateJointAccountHolderRepository repository, AccountHolderCreatedSNSTopicPublisher snsTopicPublisher,
                                           Clock currentInstant, Validator validator) {
        this.repository = repository;
        this.snsTopicPublisher = snsTopicPublisher;
        this.currentInstant = currentInstant;
        this.validator = validator;
    }

    public AccountHolderDTO create(CreateJointAccountHolderRequest createJointAccountHolderRequest) {
        LOGGER.debug("Starting the creation process for a new account holder.");

        var constraintViolations = validator.validate(createJointAccountHolderRequest);
        if (!constraintViolations.isEmpty()) {
            if (LOGGER.isWarnEnabled()) {
                var validationMessages = constraintViolations.stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                LOGGER.warn("Validation errors: {}", validationMessages);
            }
            throw new ConstraintViolationException(constraintViolations);
        }

        var jointAccountHolder = buildJointAccountHolder(createJointAccountHolderRequest.bankAccountId(), createJointAccountHolderRequest.accountHolderRequest());
        repository.create(jointAccountHolder);

        var accountHolderDTO = new AccountHolderDTO(jointAccountHolder);
        snsTopicPublisher.publishMessage(accountHolderDTO);

        return accountHolderDTO;
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
