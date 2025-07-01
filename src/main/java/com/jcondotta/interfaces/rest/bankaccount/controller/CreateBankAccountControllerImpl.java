package com.jcondotta.interfaces.rest.bankaccount.controller;

import com.jcondotta.interfaces.rest.BankAccountMapper;
import com.jcondotta.application.usecase.bankaccount.CreateBankAccountUseCase;
import com.jcondotta.application.ports.input.controller.CreateBankAccountController;
import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountRequest;
import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountResponse;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("${api.v1.root-path}")
@AllArgsConstructor
public class CreateBankAccountControllerImpl implements CreateBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountControllerImpl.class);

    private final CreateBankAccountUseCase createBankAccountUseCase;
    private final BankAccountURIConfiguration bankAccountURIConfig;
    private final BankAccountMapper bankAccountMapper;

    @Override
    public ResponseEntity<CreateBankAccountResponse> createBankAccount(CreateBankAccountRequest request) {
        LOGGER.info("Received request to create bank account for the account holder: {}", request);

        var bankAccount = createBankAccountUseCase.execute(request);

        LOGGER.atDebug()
                .setMessage("Bank account created successfully")
                .addKeyValue("bankAccountId", bankAccount.bankAccountId().toString())
                .log();

        return ResponseEntity
                .created(bankAccountURIConfig
                    .bankAccountURI(bankAccount.bankAccountId().value()))
                .body(bankAccountMapper.toCreateBankAccountResponse(bankAccount));
    }
}