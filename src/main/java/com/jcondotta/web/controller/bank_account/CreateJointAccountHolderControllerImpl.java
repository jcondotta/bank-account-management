package com.jcondotta.web.controller.bank_account;

import com.jcondotta.application.CreateJointAccountHolderUseCase;
import com.jcondotta.application.dto.create.CreateJointAccountHolderRequest;
import com.jcondotta.application.dto.create.CreateJointAccountHolderResponse;
import com.jcondotta.application.ports.input.controller.CreateJointAccountHolderController;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.domain.value_object.BankAccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
public class CreateJointAccountHolderControllerImpl implements CreateJointAccountHolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderControllerImpl.class);

    private final CreateJointAccountHolderUseCase createJointAccountHolderUseCase;
    private final BankAccountURIConfiguration bankAccountURIConfig;

    public CreateJointAccountHolderControllerImpl(CreateJointAccountHolderUseCase createJointAccountHolderUseCase,
                                                  BankAccountURIConfiguration bankAccountURIConfig) {
        this.createJointAccountHolderUseCase = createJointAccountHolderUseCase;
        this.bankAccountURIConfig = bankAccountURIConfig;
    }
    @Override
    public ResponseEntity<CreateJointAccountHolderResponse> createJointAccountHolder(UUID bankAccountId, CreateJointAccountHolderRequest request) {
        LOGGER.atInfo()
                .setMessage("Received request to create a joint account holder for Bank Account ID")
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var response = createJointAccountHolderUseCase
            .createJointAccountHolder(new BankAccountId(bankAccountId), request);

        return ResponseEntity
                .created(bankAccountURIConfig.accountHolderURI(bankAccountId, response.accountHolderId().value()))
                .body(response);
    }
}