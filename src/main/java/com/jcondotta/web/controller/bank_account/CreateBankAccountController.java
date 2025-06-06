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
            summary = "${operation.createBankAccount.summary}",
            description = "${operation.createBankAccount.description}",
            tags = {"Bank Accounts"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "${requestBody.createBankAccount.description}",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateAccountHolderRequest.class)))
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "${response.createBankAccount.201.description}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BankAccountDTO.class)),
                    headers = {
                            @Header(name = "Location", description = "${response.createBankAccount.201.header.Location.description}",
                                    schema = @Schema(type = "string", format = "uri",
                                            example = "${response.createBankAccount.201.header.Location.example}"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "${response.createBankAccount.400.description}"),
            @ApiResponse(responseCode = "500", description = "${response.createBankAccount.500.description}")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BankAccountDTO> createBankAccount(@Valid @RequestBody CreateAccountHolderRequest request) {
        LOGGER.info("Received request to create bank account for the account holder: {}", request);

        var bankAccountDTO = createBankAccountUseCase.createBankAccount(request);

//        bankAccountDTO.getPrimaryAccountHolder()
//                .ifPresent(holder -> MDC.put("accountHolderId", holder.getAccountHolderId().toString()));

        LOGGER.info("Bank account created successfully");
        LOGGER.atInfo()
                .setMessage("Bank account created successfully")
                .addKeyValue("bankAccountId", bankAccountDTO.getBankAccountId().toString())
//                .addKeyValue("accountHolderId", bankAccountDTO.getAccountHolderId().toString())
                .log();

//        return ResponseEntity.created(URI.create("asd")).build();//created(bankAccountDTO, bankAccountURIConfig.bankAccountURI(bankAccountDTO.getBankAccountId()));
        return ResponseEntity.created(bankAccountURIConfig.bankAccountURI(bankAccountDTO.getBankAccountId())).body(bankAccountDTO);
    }
}
