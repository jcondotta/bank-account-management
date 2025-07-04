package com.jcondotta.interfaces.rest.removejointaccountholder.controller;

import com.jcondotta.application.usecase.removejointaccountholder.RemoveJointAccountHolderUseCase;
import com.jcondotta.interfaces.rest.removejointaccountholder.mapper.RemoveJointAccountHolderRequestControllerMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("${api.v1.account-holder-path}")
@AllArgsConstructor
public class RemoveJointAccountHolderControllerImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveJointAccountHolderControllerImpl.class);

    private final RemoveJointAccountHolderUseCase removeJointAccountHolderUseCase;
    private final RemoveJointAccountHolderRequestControllerMapper requestControllerMapper;

    @DeleteMapping
    public ResponseEntity<Void> removeJointAccountHolder(
        @PathVariable("bank-account-id") UUID bankAccountId,
        @PathVariable("account-holder-id") UUID accountHolderId) {

        LOGGER.atInfo()
                .setMessage("Received request to remove a joint account holder from bank account")
                .log();

        removeJointAccountHolderUseCase.execute(
            requestControllerMapper.toUseCaseCommand(bankAccountId, accountHolderId)
        );
        return ResponseEntity.noContent().build();
    }
}