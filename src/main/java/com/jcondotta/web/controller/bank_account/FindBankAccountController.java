package com.jcondotta.web.controller.bank_account;

import com.jcondotta.FindBankAccountByIdUseCase;
import com.jcondotta.service.dto.BankAccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("${api.v1.bank-account-path}")
public class FindBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBankAccountController.class);

    private final FindBankAccountByIdUseCase findBankAccountUseCase;

    public FindBankAccountController(FindBankAccountByIdUseCase findBankAccountUseCase) {
        this.findBankAccountUseCase = findBankAccountUseCase;
    }

    @Operation(
            tags = {"Bank accounts"},
            summary = "${findBankAccount.operation.summary}",
            description = "${findBankAccount.operation.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "${findBankAccount.response.200.description}",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = BankAccountDTO.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "${findBankAccount.response.404.description}"),
        @ApiResponse(responseCode = "400", description = "${findBankAccount.response.400.description}"),
        @ApiResponse(responseCode = "500", description = "${findBankAccount.response.500.description}")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccountDTO> findBankAccount(
        @Parameter(
                description = "${findBankAccount.parameter.bankAccountId.description}",
                required = true,
                example = "01920bff-1338-7efd-ade6-e9128debe5d4"
        )
        @PathVariable("bank-account-id") UUID bankAccountId) {

        LOGGER.atInfo()
                .setMessage("Received request to fetch bank account with ID: {}")
                .addArgument(bankAccountId)
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        BankAccountDTO dto = findBankAccountUseCase.findBankAccountById(bankAccountId);
        return ResponseEntity.ok(dto);
    }
}