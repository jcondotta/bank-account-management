package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.repository.CreateJointAccountHolderRepository;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class CreateJointAccountHolderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderService.class);

    private final CreateJointAccountHolderRepository createJointAccountHolderRepository;
    private final Validator validator;

    @Inject
    public CreateJointAccountHolderService(CreateJointAccountHolderRepository createJointAccountHolderRepository, Validator validator) {
        this.createJointAccountHolderRepository = createJointAccountHolderRepository;
        this.validator = validator;
    }

    public AccountHolderDTO create(CreateJointAccountHoldersRequest createJointAccountHoldersRequest) {
        LOGGER.debug("Starting the creation process for a new bank account and its primary account holder.");

        var constraintViolations = validator.validate(createJointAccountHoldersRequest);
        if (!constraintViolations.isEmpty()) {
            LOGGER.warn("Validation errors: {}", constraintViolations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", ")));

            throw new ConstraintViolationException(constraintViolations);
        }

        var jointAccountHolders = buildJointAccountHolders(createJointAccountHoldersRequest);
        createJointAccountHolderRepository.create(jointAccountHolders);

        return null;
    }

    private List<BankingEntity> buildJointAccountHolders(CreateJointAccountHoldersRequest createJointAccountHoldersRequest) {
        return createJointAccountHoldersRequest.accountHolderRequests()
                .stream()
                .map(accountHolderRequest -> BankingEntity.buildAccountHolder(
                        UUID.randomUUID(),
                        accountHolderRequest.accountHolderName(),
                        accountHolderRequest.passportNumber(),
                        accountHolderRequest.dateOfBirth(),
                        AccountHolderType.JOINT,
                        createJointAccountHoldersRequest.bankAccountId()
                    )
                )
                .toList();
    }
}
