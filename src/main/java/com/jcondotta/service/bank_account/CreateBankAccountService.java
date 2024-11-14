package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
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
        LOGGER.debug("Starting the creation process for a new bank account and its primary account holder.");

        var constraintViolations = validator.validate(createBankAccountRequest);
        if (!constraintViolations.isEmpty()) {
            LOGGER.warn("Validation errors: {}", constraintViolations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", ")));

            throw new ConstraintViolationException(constraintViolations);
        }

        var bankAccount = buildBankAccount();
        var accountHolder = buildPrimaryAccountHolder(bankAccount.getBankAccountId(), createBankAccountRequest);

        var createBankAccountResponse = createBankAccountRepository.create(bankAccount, accountHolder);
        var bankAccountDTO = createBankAccountResponse.bankAccountDTO();

        return bankAccountDTO;
    }

    private BankingEntity buildPrimaryAccountHolder(UUID bankAccountId, CreateBankAccountRequest createBankAccountRequest) {
        return BankingEntity.buildAccountHolder(
                UUID.randomUUID(),
                createBankAccountRequest.accountHolder().accountHolderName(),
                createBankAccountRequest.accountHolder().passportNumber(),
                createBankAccountRequest.accountHolder().dateOfBirth(),
                AccountHolderType.PRIMARY,
                bankAccountId
        );
    }

    private BankingEntity buildBankAccount() {
        var generatedIban = new Faker().finance().iban();
        return BankingEntity.buildBankAccount(UUID.randomUUID(), generatedIban, LocalDateTime.now(currentInstant));
    }
}
