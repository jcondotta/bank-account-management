package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.event.AccountHolderCreatedSNSTopicPublisher;
import com.jcondotta.repository.CreateBankAccountRepository;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateBankAccountRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
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
    private final AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;
    private final Clock currentInstant;
    private final Validator validator;

    @Inject
    public CreateBankAccountService(CreateBankAccountRepository createBankAccountRepository, AccountHolderCreatedSNSTopicPublisher snsTopicPublisher,
                                    Clock currentClock, Validator validator) {
        this.createBankAccountRepository = createBankAccountRepository;
        this.snsTopicPublisher = snsTopicPublisher;
        this.currentInstant = currentClock;
        this.validator = validator;
    }

    public BankAccountDTO create(@NotNull AccountHolderRequest accountHolderRequest) {
        LOGGER.debug("Starting the creation process for a new bank account and its primary account holder.");

        var constraintViolations = validator.validate(accountHolderRequest);
        if (!constraintViolations.isEmpty()) {
            if (LOGGER.isWarnEnabled()) {
                var validationMessages = constraintViolations.stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                LOGGER.warn("Validation errors: {}", validationMessages);
            }
            throw new ConstraintViolationException(constraintViolations);
        }

        var bankAccount = buildBankAccount();
        var accountHolder = buildPrimaryAccountHolder(bankAccount.getBankAccountId(), accountHolderRequest);

        createBankAccountRepository.create(bankAccount, accountHolder);
        var bankAccountDTO = new BankAccountDTO(bankAccount, accountHolder);

        snsTopicPublisher.publishMessage(bankAccountDTO.getPrimaryAccountHolder()
                .orElseThrow(() -> new IllegalStateException("Primary account holder was not found for the created bank account: "
                        + bankAccountDTO.getBankAccountId())));

        return bankAccountDTO;
    }

    private BankingEntity buildPrimaryAccountHolder(UUID bankAccountId, AccountHolderRequest accountHolderRequest) {
        return BankingEntity.buildAccountHolder(
                UUID.randomUUID(),
                accountHolderRequest.accountHolderName(),
                accountHolderRequest.passportNumber(),
                accountHolderRequest.dateOfBirth(),
                AccountHolderType.PRIMARY,
                LocalDateTime.now(currentInstant),
                bankAccountId
        );
    }

    private BankingEntity buildBankAccount() {
        var generatedIban = new Faker().finance().iban();
        return BankingEntity.buildBankAccount(UUID.randomUUID(), generatedIban, LocalDateTime.now(currentInstant));
    }
}
