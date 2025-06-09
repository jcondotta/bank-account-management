package com.jcondotta.web.controller.bank_account;

import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.service.bank_account.CreateJointAccountHolderService;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHolderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("${api.v1.bank-account-path}")
public class CreateJointAccountHolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderController.class);

    private final CreateJointAccountHolderService createJointAccountHolderService;
    private final BankAccountURIConfiguration bankAccountURIConfig;

    public CreateJointAccountHolderController(CreateJointAccountHolderService createJointAccountHolderService,
                                              BankAccountURIConfiguration bankAccountURIConfig) {
        this.createJointAccountHolderService = createJointAccountHolderService;
        this.bankAccountURIConfig = bankAccountURIConfig;
    }

    @Operation(
            summary = "${operation.createJointAccountHolder.summary}",
            description = "${operation.createJointAccountHolder.description}",
            tags = {"joint account holders"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "${requestBody.createJointAccountHolder.description}",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateAccountHolderRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "${response.createJointAccountHolder.201.description}",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateAccountHolderRequest.class)
                    ),
                    headers = {
                            @Header(
                                    name = "Location",
                                    description = "${response.createJointAccountHolder.201.header.Location.description}",
                                    schema = @Schema(
                                            type = "string",
                                            format = "uri",
                                            example = "${response.createJointAccountHolder.201.header.Location.example}"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "${response.createJointAccountHolder.400.description}"),
            @ApiResponse(responseCode = "500", description = "${response.createJointAccountHolder.500.description}")
    })
    @PostMapping(value = "/account-holders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountHolderDTO> createJointAccountHolder(
            @Parameter(
                    description = "${parameter.createJointAccountHolder.bankAccountId.description}",
                    required = true,
                    example = "01920bff-1338-7efd-ade6-e9128debe5d4"
            )
            @PathVariable("bank-account-id") UUID bankAccountId,
            @Valid @RequestBody CreateAccountHolderRequest request) {

        LOGGER.atInfo()
                .setMessage("Received request to create a joint account holder for Bank Account ID")
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var createJointAccountHolderRequest = new CreateJointAccountHolderRequest(bankAccountId, request);
        var accountHolderDTO = createJointAccountHolderService.create(createJointAccountHolderRequest);

        return ResponseEntity
                .created(bankAccountURIConfig.bankAccountURI(bankAccountId))
                .body(accountHolderDTO);
    }
}