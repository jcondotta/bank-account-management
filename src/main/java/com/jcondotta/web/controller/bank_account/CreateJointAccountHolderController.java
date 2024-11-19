package com.jcondotta.web.controller.bank_account;

import com.jcondotta.service.bank_account.CreateJointAccountHolderService;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Validated
@Controller(BankAccountURIBuilder.BANK_ACCOUNT_API_V1_MAPPING)
public class CreateJointAccountHolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateJointAccountHolderController.class);

    private final CreateJointAccountHolderService createJointAccountHolderService;

    @Inject
    public CreateJointAccountHolderController(CreateJointAccountHolderService createJointAccountHolderService) {
        this.createJointAccountHolderService = createJointAccountHolderService;
    }

//    @Operation(summary = "${operation.createBankAccount.summary}", description = "${operation.createBankAccount.description}",
//            requestBody = @RequestBody(
//                    description = "${requestBody.createBankAccount.description}",
//                    required = true,
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreateBankAccountRequest.class)))
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "201", description = "${response.createBankAccount.201.description}",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BankAccountDTO.class)),
//                    headers = {
//                            @Header(name = "Location", description = "${response.createBankAccount.201.header.Location.description}",
//                                    schema = @Schema(type = "string", format = "uri",
//                                            example = "${response.createBankAccount.201.header.Location.example}"
//                                    )
//                            )
//                    }
//            ),
//            @ApiResponse(responseCode = "400", description = "${response.createBankAccount.400.description}"),
//            @ApiResponse(responseCode = "500", description = "${response.createBankAccount.500.description}")
//    })
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.CREATED)
    public HttpResponse<AccountHolderDTO> createJointAccountHolder(@PathVariable("bank-account-id") UUID bankAccountId
            , @Body AccountHolderRequest accountHolderRequest) {
//        LOGGER.info("Received request to create bank account for the account holder: {}", createBankAccountRequest.accountHolder());

        var createJointAccountHolderRequest = new CreateJointAccountHoldersRequest(bankAccountId, accountHolderRequest);
        var accountHolderDTO = createJointAccountHolderService.create(createJointAccountHolderRequest);

        return HttpResponse.created(accountHolderDTO, BankAccountURIBuilder.bankAccountURI(accountHolderDTO.getBankAccountId()));


//        try {
//            var bankAccountDTO = createBankAccountService.create(createBankAccountRequest);
//            MDC.put("bankAccountId", bankAccountDTO.getBankAccountId().toString());
//
//            bankAccountDTO.getPrimaryAccountHolder()
//                    .ifPresent(accountHolderDTO -> MDC.put("accountHolderId", accountHolderDTO.getAccountHolderId().toString()));
//
//            LOGGER.info("Bank account created successfully");
//            return HttpResponse.created(bankAccountDTO, BankAccountURIBuilder.bankAccountURI(bankAccountDTO.getBankAccountId()));
//        }
//        finally {
//            MDC.clear();
//        }
    }
}
