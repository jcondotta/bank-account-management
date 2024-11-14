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

    @Operation(summary = "Retrieve bank account details", description = "Fetches details of a bank account by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bank account successfully retrieved.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BankAccountDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank account not found for the provided ID."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data. The provided bank account ID is malformed."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Unable to process the request at this time."
            )
    })
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<BankAccountDTO> findBankAccount(
            @Parameter(description = "Unique identifier of the bank account", required = true, example = "01920bff-1338-7efd-ade6-e9128debe5d4")
            @PathVariable("bank-account-id") UUID bankAccountId) {

        LOGGER.info("Received request to fetch bank account with ID: {}", bankAccountId);

        return findBankAccountService.findBankAccountById(bankAccountId)
                .map(account -> {
                    LOGGER.info("Bank account found with ID: {}", bankAccountId);
                    return HttpResponse.ok(account);
                })
                .orElseGet(() -> {
                    LOGGER.warn("No bank account found with ID: {}", bankAccountId);
                    return HttpResponse.notFound();
                });
    }
}
