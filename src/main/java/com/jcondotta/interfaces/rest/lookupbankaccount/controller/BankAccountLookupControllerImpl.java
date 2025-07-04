package com.jcondotta.interfaces.rest.lookupbankaccount.controller;

import com.jcondotta.application.ports.input.controller.BankAccountLookupController;
import com.jcondotta.application.usecase.lookupbankaccount.BankAccountLookupUseCase;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.interfaces.rest.lookupbankaccount.mapper.BankAccountLookupResponseControllerMapper;
import com.jcondotta.interfaces.rest.lookupbankaccount.model.BankAccountLookupRestResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("${api.v1.bank-account-path}")
@AllArgsConstructor
public class BankAccountLookupControllerImpl implements BankAccountLookupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountLookupControllerImpl.class);

    private final BankAccountLookupUseCase bankAccountLookupUseCase;
    private final BankAccountLookupResponseControllerMapper responseControllerMapper;

    public ResponseEntity<BankAccountLookupRestResponse> findBankAccount(UUID bankAccountId) {
        LOGGER.atInfo()
                .setMessage("Received request to retrieve bank account with id: {}")
                .addArgument(bankAccountId)
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var bankAccountLookupResult = bankAccountLookupUseCase.lookup(BankAccountId.of(bankAccountId));
        return ResponseEntity.ok(responseControllerMapper.toResponse(bankAccountLookupResult));
    }
}