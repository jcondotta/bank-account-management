package com.jcondotta.interfaces.rest.createbankaccount.controller;

import com.jcondotta.application.ports.input.controller.CreateBankAccountController;
import com.jcondotta.application.usecase.createbankaccount.CreateBankAccountUseCase;
import com.jcondotta.infrastructure.properties.BankAccountURIConfiguration;
import com.jcondotta.interfaces.rest.createbankaccount.mapper.CreateBankAccountRequestControllerMapper;
import com.jcondotta.interfaces.rest.createbankaccount.mapper.CreateBankAccountResponseControllerMapper;
import com.jcondotta.interfaces.rest.createbankaccount.model.CreateBankAccountRestRequest;
import com.jcondotta.interfaces.rest.createbankaccount.model.CreateBankAccountRestResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@Validated
@RestController
@RequestMapping("${api.v1.root-path}")
@AllArgsConstructor
public class CreateBankAccountControllerImpl implements CreateBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountControllerImpl.class);

    private final CreateBankAccountUseCase createBankAccountUseCase;
    private final BankAccountURIConfiguration bankAccountURIConfig;
    private final CreateBankAccountRequestControllerMapper requestMapper;
    private final CreateBankAccountResponseControllerMapper responseMapper;

    @Override
    public ResponseEntity<CreateBankAccountRestResponse> createBankAccount(CreateBankAccountRestRequest restRequest) {
        LOGGER.info("Received request to create bank account and primary account holder");

        var createBankAccountRequest = requestMapper.toUseCaseCommand(restRequest);
        var createBankAccountResponse = createBankAccountUseCase.execute(createBankAccountRequest);

//        LOGGER.atDebug()
//                .setMessage("Bank account created successfully.")
//                .addKeyValue("bankAccountId", createBankAccountResponse.bankAccountId().toString())
//                .log();

        return ResponseEntity
                .created(bankAccountURIConfig.bankAccountURI(createBankAccountResponse.bankAccount().bankAccountId()))
                .body(responseMapper.toResponse(createBankAccountResponse.bankAccount()));
    }
}