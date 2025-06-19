package com.jcondotta.web.controller.bank_account;

import com.jcondotta.application.BankAccountLookupUseCase;
import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.application.ports.input.controller.BankAccountLookupController;
import com.jcondotta.domain.value_object.BankAccountId;
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
public class BankAccountLookupControllerImpl implements BankAccountLookupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountLookupControllerImpl.class);

    private final BankAccountLookupUseCase bankAccountLookupUseCase;

    public BankAccountLookupControllerImpl(BankAccountLookupUseCase bankAccountLookupUseCase) {
        this.bankAccountLookupUseCase = bankAccountLookupUseCase;
    }

    public ResponseEntity<BankAccountLookupResponse> findBankAccount(UUID bankAccountId) {
        LOGGER.atInfo()
                .setMessage("Received request to retrieve bank account with id: {}")
                .addArgument(bankAccountId)
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var bankAccountLookupByIdResponse = bankAccountLookupUseCase
            .lookup(new BankAccountId(bankAccountId));

        return ResponseEntity.ok(bankAccountLookupByIdResponse);
    }
}