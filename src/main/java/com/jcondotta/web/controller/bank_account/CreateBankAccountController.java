package com.jcondotta.web.controller.bank_account;

import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.dto.ExistentBankAccountDTO;
import com.jcondotta.service.request.CreateBankAccountRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Validated
@Controller(BankAccountURIBuilder.BASE_PATH_API_V1_MAPPING)
public class CreateBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountController.class);

    private final CreateBankAccountService createBankAccountService;

    @Inject
    public CreateBankAccountController(CreateBankAccountService createBankAccountService) {
        this.createBankAccountService = createBankAccountService;
    }

    @Operation(summary = "Add a new bank account",
            description = "Creates a new bank account with the provided account holder information.",
            requestBody = @RequestBody(
                    description = "Bank account details",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateBankAccountRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bank account successfully created.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BankAccountDTO.class))
//                    headers = @Header(name = "Location",
//                            description = "Relative URI for retrieving all recipients for the bank account",
//                            schema = @Schema(type = "string", format = "uri", example = "/api/v1/recipients/bank-account-id/{bankAccountId}"))
            ),
            @ApiResponse(responseCode = "200", description = "Bank account already exists, returning the existing data.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExistentBankAccountDTO.class))),
//            @ApiResponse(responseCode = "400", description = "Invalid bank account ID, recipient name, or IBAN. Ensure that all required fields are valid."),
            @ApiResponse(responseCode = "500", description = "Internal server error. This may occur due to system issues, failed database connections, or unexpected runtime exceptions.")
    })
    @Status(HttpStatus.CREATED)
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<BankAccountDTO> createBankAccount(@Body CreateBankAccountRequest createBankAccountRequest) {
        LOGGER.info("Received request to create bank account for the account holder: {}", createBankAccountRequest.accountHolder());

        BankAccountDTO bankAccountDTO = createBankAccountService.create(createBankAccountRequest);

        if (bankAccountDTO instanceof ExistentBankAccountDTO) {
            LOGGER.info("[BankAccountId={}] Returning existing bank account", bankAccountDTO.getBankAccountId());

            return HttpResponse.ok(bankAccountDTO);
        }
        else {
            LOGGER.info("Bank account created with ID: {}", bankAccountDTO.getBankAccountId());
            return HttpResponse.created(bankAccountDTO, BankAccountURIBuilder.bankAccountURI(bankAccountDTO.getBankAccountId()));
        }
    }
}