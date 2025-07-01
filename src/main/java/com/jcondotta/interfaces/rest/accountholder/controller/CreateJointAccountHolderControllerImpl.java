package com.jcondotta.interfaces.rest.accountholder.controller;

import com.jcondotta.interfaces.rest.BankAccountMapper;
import com.jcondotta.application.usecase.accountholder.CreateJointAccountHolderUseCase;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderRequest;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderResponse;
import com.jcondotta.application.ports.input.controller.CreateJointAccountHolderController;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
public class CreateJointAccountHolderControllerImpl implements CreateJointAccountHolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderControllerImpl.class);

    private final CreateJointAccountHolderUseCase createJointAccountHolderUseCase;
    private final BankAccountURIConfiguration bankAccountURIConfig;
    private final BankAccountMapper bankAccountMapper;

    @Override
    public ResponseEntity<CreateJointAccountHolderResponse> createJointAccountHolder(UUID bankAccountId, CreateJointAccountHolderRequest request) {
        LOGGER.atInfo()
                .setMessage("Received request to create a joint account holder for Bank Account ID")
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var accountHolder = createJointAccountHolderUseCase
            .execute(new BankAccountId(bankAccountId), request);

        return ResponseEntity.ok(null);
//                .created(bankAccountURIConfig.accountHolderURI(bankAccountId, accountHolder.accountHolderId().value()))
//                .body(bankAccountMapper.toCreateJointAccountHolderResponse(accountHolder));
    }
}