package com.jcondotta.interfaces.rest.addjointaccountholder.controller;

import com.jcondotta.application.ports.input.controller.AddJointAccountHolderController;
import com.jcondotta.application.usecase.addjointaccountholder.AddJointAccountHolderUseCase;
import com.jcondotta.interfaces.rest.addjointaccountholder.mapper.AddJointAccountHolderRequestControllerMapper;
import com.jcondotta.interfaces.rest.addjointaccountholder.mapper.AddJointAccountHolderResponseControllerMapper;
import com.jcondotta.interfaces.rest.addjointaccountholder.model.AddJointAccountHolderRestRequest;
import com.jcondotta.interfaces.rest.addjointaccountholder.model.AddJointAccountHolderRestResponse;
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
public class AddJointAccountHolderControllerImpl implements AddJointAccountHolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddJointAccountHolderControllerImpl.class);

    private final AddJointAccountHolderUseCase addJointAccountHolderUseCase;
    private final AddJointAccountHolderRequestControllerMapper requestControllerMapper;
    private final AddJointAccountHolderResponseControllerMapper responseControllerMapper;

    @Override
    public ResponseEntity<AddJointAccountHolderRestResponse> createJointAccountHolder(UUID bankAccountId, AddJointAccountHolderRestRequest restRequest) {
        LOGGER.atInfo()
                .setMessage("Received request to create a joint account holder for Bank Account ID")
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var useCaseCommand = requestControllerMapper.toUseCaseCommand(bankAccountId, restRequest);
        var addJointAccountHolderResult = addJointAccountHolderUseCase.execute(useCaseCommand);
        return ResponseEntity.ok(
            responseControllerMapper.toResponse(addJointAccountHolderResult)
        );
    }
}