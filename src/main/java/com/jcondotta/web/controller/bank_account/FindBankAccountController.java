package com.jcondotta.web.controller.bank_account;

import com.jcondotta.service.bank_account.FindBankAccountService;
import com.jcondotta.service.dto.BankAccountDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Validated
@Controller(BankAccountURIBuilder.BANK_ACCOUNT_API_V1_MAPPING)
public class FindBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBankAccountController.class);

    private final FindBankAccountService findBankAccountService;

    @Inject
    public FindBankAccountController(FindBankAccountService findBankAccountService) {
        this.findBankAccountService = findBankAccountService;
    }

    @Operation(
            summary = "${operation.findBankAccount.summary}",
            description = "${operation.findBankAccount.description}"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "${response.findBankAccount.200.description}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BankAccountDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "${response.findBankAccount.404.description}"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "${response.findBankAccount.400.description}"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "${response.findBankAccount.500.description}"
            )
    })
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<BankAccountDTO> findBankAccount(
            @Parameter(
                    description = "${parameter.findBankAccount.bankAccountId.description}", required = true,
                    example = "${parameter.findBankAccount.bankAccountId.example}"
            )
            @PathVariable("bank-account-id") UUID bankAccountId) {

        LOGGER.info("Received request to fetch bank account with ID: {}", bankAccountId);

        var bankAccountDTO = findBankAccountService.findBankAccountById(bankAccountId);
        return HttpResponse.ok(bankAccountDTO);
    }
}
