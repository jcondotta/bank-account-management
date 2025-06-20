package com.jcondotta.web.controller.bank_account;

import com.jcondotta.application.CreateBankAccountUseCase;
import com.jcondotta.application.ports.input.controller.CreateBankAccountController;
import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;
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

    @Override
    public ResponseEntity<CreateBankAccountResponse> createBankAccount(CreateBankAccountRequest request) {
        LOGGER.info("Received request to create bank account for the account holder: {}", request);

        var createBankAccountResponse = createBankAccountUseCase.createBankAccount(request);

        LOGGER.atDebug()
                .setMessage("Bank account created successfully")
                .addKeyValue("bankAccountId", createBankAccountResponse.bankAccount().getBankAccountId().toString())
                .log();

        return ResponseEntity
                .created(bankAccountURIConfig
                    .bankAccountURI(createBankAccountResponse.bankAccount().getBankAccountId()))
                .body(createBankAccountResponse);
    }
}