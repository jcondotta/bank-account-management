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
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Validated
@Controller(BankAccountURIBuilder.BASE_PATH_API_V1_MAPPING)
public class CreateBankAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountController.class);

    private final CreateBankAccountService createBankAccountService;

    @Inject
    public CreateBankAccountController(CreateBankAccountService createBankAccountService) {
        this.createBankAccountService = createBankAccountService;
    }

    @Operation(summary = "${operation.createBankAccount.summary}", description = "${operation.createBankAccount.description}",
            requestBody = @RequestBody(
                    description = "${requestBody.createBankAccount.description}",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreateBankAccountRequest.class)))
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "${response.createBankAccount.201.description}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BankAccountDTO.class)),
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
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.CREATED)
    public HttpResponse<BankAccountDTO> createBankAccount(@Body CreateBankAccountRequest createBankAccountRequest) {
        LOGGER.info("Received request to create bank account for the account holder: {}", createBankAccountRequest.accountHolder());

        try {
            var bankAccountDTO = createBankAccountService.create(createBankAccountRequest);
            MDC.put("bankAccountId", bankAccountDTO.getBankAccountId().toString());

            bankAccountDTO.getPrimaryAccountHolder()
                    .ifPresent(accountHolderDTO -> MDC.put("accountHolderId", accountHolderDTO.getAccountHolderId().toString()));

            LOGGER.info("Bank account created successfully");
            return HttpResponse.created(bankAccountDTO, BankAccountURIBuilder.bankAccountURI(bankAccountDTO.getBankAccountId()));
        }
        finally {
            MDC.clear();
        }
    }
}
