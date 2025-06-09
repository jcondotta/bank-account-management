package com.jcondotta.web.controller.bank_account;

import com.jcondotta.CreateBankAccountUseCase;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import io.swagger.v3.oas.annotations.Operation;
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

@Validated
@RestController
@RequestMapping("${api.v1.root-path}")
public class CreateBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountController.class);

    private final CreateBankAccountUseCase createBankAccountUseCase;
    private final BankAccountURIConfiguration bankAccountURIConfig;

    public CreateBankAccountController(CreateBankAccountUseCase createBankAccountUseCase,
                                       BankAccountURIConfiguration bankAccountURIConfig) {
        this.createBankAccountUseCase = createBankAccountUseCase;
        this.bankAccountURIConfig = bankAccountURIConfig;
    }

    @Operation(
            tags = {"Bank accounts"},
            summary = "${createBankAccount.operation.summary}",
            description = "${createBankAccount.operation.description}",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "${createBankAccount.requestBody.description}",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateAccountHolderRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "${createBankAccount.response.201.description}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BankAccountDTO.class)),
                    headers = {
                            @Header(name = "Location", description = "URI of the newly created bank account",
                                    schema = @Schema(type = "string", format = "uri",
                                            example = "/api/v1/bank-accounts/01920bff-1338-7efd-ade6-e9128debe5d4"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "${createBankAccount.response.400.description}"),
            @ApiResponse(responseCode = "500", description = "${createBankAccount.response.500.description}")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BankAccountDTO> createBankAccount(@Valid @RequestBody CreateAccountHolderRequest request) {
        LOGGER.info("Received request to create bank account for the account holder: {}", request);

        var bankAccountDTO = createBankAccountUseCase.createBankAccount(request);

        LOGGER.atInfo()
                .setMessage("Bank account created successfully")
                .addKeyValue("bankAccountId", bankAccountDTO.getBankAccountId().toString())
                .log();

        return ResponseEntity
                .created(bankAccountURIConfig.bankAccountURI(bankAccountDTO.getBankAccountId()))
                .body(bankAccountDTO);
    }
}