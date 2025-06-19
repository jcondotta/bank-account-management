package com.jcondotta.web.controller.bank_account;

import com.jcondotta.application.AccountHolderLookupUseCase;
import com.jcondotta.application.dto.lookup.AccountHolderLookupResponse;
import com.jcondotta.application.ports.input.controller.AccountHolderLookupController;
import com.jcondotta.infrastructure.ports.output.repository.AccountHolderLookupRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
public class AccountHolderLookupControllerImpl implements AccountHolderLookupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderLookupControllerImpl.class);

    private final AccountHolderLookupUseCase accountHolderLookupUseCase;

    @Override
    public ResponseEntity<AccountHolderLookupResponse> accountHolderLookup(UUID bankAccountId, UUID accountHolderId) {
        LOGGER.atInfo()
            .setMessage("Received request to retrieve bank account with id: {} and account holder with id: {}")
            .addArgument(bankAccountId)
            .addArgument(accountHolderId)
            .log();

        var accountHolderLookupRequest = new AccountHolderLookupRequest(bankAccountId, accountHolderId);
        var accountHolderLookupResponse = accountHolderLookupUseCase.lookup(accountHolderLookupRequest);
        return ResponseEntity.ok(accountHolderLookupResponse);
    }
}