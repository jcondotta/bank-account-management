package com.jcondotta.service.bank_account;

import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.AccountHolderType;
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
import org.slf4j.MDC;

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

        try {
            var constraintViolations = validator.validate(createBankAccountRequest);
            if (!constraintViolations.isEmpty()) {
                LOGGER.warn("Validation errors: {}", constraintViolations.stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .collect(Collectors.joining(", ")));

                throw new ConstraintViolationException(constraintViolations);
            }

            var bankAccount = buildBankAccount();
            var accountHolder = buildPrimaryAccountHolder(bankAccount.getBankAccountId(), createBankAccountRequest);

            MDC.put("bankAccountId", bankAccount.getBankAccountId().toString());
            MDC.put("accountHolderId", accountHolder.getAccountHolderId().toString());
            MDC.put("accountHolderName", accountHolder.getAccountHolderName());

            var createBankAccountResponse = createBankAccountRepository.create(bankAccount, accountHolder);

            var bankAccountDTO = createBankAccountResponse.bankAccountDTO();

            return bankAccountDTO;
        }
        finally {
            MDC.clear();
        }
    }

    private AccountHolder buildPrimaryAccountHolder(UUID bankAccountId, CreateBankAccountRequest createBankAccountRequest) {
        return new AccountHolder(
                bankAccountId,
                UUID.randomUUID(),
                createBankAccountRequest.accountHolder().accountHolderName(),
                createBankAccountRequest.accountHolder().passportNumber(),
                createBankAccountRequest.accountHolder().dateOfBirth(),
                AccountHolderType.PRIMARY
        );
    }

    private BankAccount buildBankAccount() {
        var generatedIban = new Faker().finance().iban();
        return new BankAccount(UUID.randomUUID(), generatedIban, LocalDateTime.now(currentInstant));
    }
}
