package com.jcondotta.web.controller.bank_account;

import com.jcondotta.application.AccountHoldersQueryUseCase;
import com.jcondotta.application.ports.input.controller.AccountHoldersQueryController;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
public class AccountHoldersQueryControllerImpl implements AccountHoldersQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHoldersQueryControllerImpl.class);

    private final AccountHoldersQueryUseCase accountHoldersQueryUseCase;

    public AccountHoldersQueryControllerImpl(AccountHoldersQueryUseCase accountHoldersQueryUseCase) {
        this.accountHoldersQueryUseCase = accountHoldersQueryUseCase;
    }

    @Override
    public ResponseEntity<AccountHoldersQueryResponse> findBankAccount(UUID bankAccountId) {
        LOGGER.atInfo()
            .setMessage("Processing lookup request for account holders of bank account with ID: {}")
            .addArgument(bankAccountId)
            .addKeyValue("bankAccountId", bankAccountId)
            .log();

        var response = accountHoldersQueryUseCase.query(new BankAccountId(bankAccountId));
        return ResponseEntity.ok(response);
    }
}