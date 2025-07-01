package com.jcondotta.interfaces.rest.accountholder.controller;

import com.jcondotta.interfaces.rest.BankAccountMapper;
import com.jcondotta.application.usecase.accountholder.AccountHolderLookupUseCase;
import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupResponse;
import com.jcondotta.application.ports.input.controller.AccountHolderLookupController;
import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupRequest;
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
    private final BankAccountMapper bankingEntityMapper;

    @Override
    public ResponseEntity<AccountHolderLookupResponse> accountHolderLookup(UUID bankAccountId, UUID accountHolderId) {
        LOGGER.atInfo()
            .setMessage("Received request to retrieve account holder with id: {} and bank account with id: {}")
            .addArgument(accountHolderId)
            .addArgument(bankAccountId)
            .log();

        var accountHolderLookupRequest = new AccountHolderLookupRequest(bankAccountId, accountHolderId);
        var accountHolder = accountHolderLookupUseCase.lookup(accountHolderLookupRequest);
        return ResponseEntity.ok(bankingEntityMapper.toAccountHolderLookupResponse(accountHolder));
    }
}