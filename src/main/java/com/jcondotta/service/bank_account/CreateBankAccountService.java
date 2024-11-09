package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.BankAccount;
import com.jcondotta.repository.CreateBankAccountRepository;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateBankAccountRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class CreateBankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountService.class);

    private final CreateBankAccountRepository createBankAccountRepository;
    private final Clock currentInstant;
    private final Validator validator;

    @Inject
    public CreateBankAccountService(CreateBankAccountRepository createBankAccountRepository, Clock currentClock, Validator validator) {
        this.createBankAccountRepository = createBankAccountRepository;
        this.currentInstant = currentClock;
        this.validator = validator;
    }

    public BankAccountDTO create(CreateBankAccountRequest createBankAccountRequest) {
        LOGGER.info("Attempting to create a new bank account.");

        validateRequest(createBankAccountRequest);

        var createBankAccountResponse = createBankAccountRepository.create(() -> buildBankAccount(createBankAccountRequest));
        var bankAccountDTO = createBankAccountResponse.bankAccountDTO();

        if (createBankAccountResponse.isIdempotent()) {
            LOGGER.debug("[BankAccountId={}] Idempotent request processed; bank account already exists", bankAccountDTO.getBankAccountId());
        }
        else {
            LOGGER.info("[BankAccountId={}] Bank account successfully created", bankAccountDTO.getBankAccountId());
            // Uncomment to publish message if needed
            // topicHandler.publishMessage(new BankAccountDTO(addBankAccountResponse.bankAccount()));
        }

        return bankAccountDTO;
    }

    private void validateRequest(CreateBankAccountRequest createBankAccountRequest) {
        var constraintViolations = validator.validate(createBankAccountRequest);
        if (!constraintViolations.isEmpty()) {
            LOGGER.warn("Validation errors: {}", constraintViolations.stream()
                            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                            .collect(Collectors.joining(", "))
            );
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private BankAccount buildBankAccount(CreateBankAccountRequest createBankAccountRequest) {
        var accountHolder = new AccountHolder(
                UUID.randomUUID(),
                createBankAccountRequest.accountHolder().accountHolderName(),
                createBankAccountRequest.accountHolder().passportNumber(),
                createBankAccountRequest.accountHolder().dateOfBirth()
        );

        return new BankAccount(
                UUID.randomUUID(),
                accountHolder,
                new Faker().finance().iban(),
                LocalDateTime.now(currentInstant));
    }
}
