package com.jcondotta.web.controller.bank_account;

import com.jcondotta.service.bank_account.CreateJointAccountHolderService;
import com.jcondotta.service.dto.AccountHoldersDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    @Operation(
            summary = "${operation.createJointAccountHolders.summary}",
            description = "${operation.createJointAccountHolders.description}",
            tags = {"Joint Accounts"},
            requestBody = @RequestBody(
                    description = "${requestBody.createJointAccountHolders.description}",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = List.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "${response.createJointAccountHolders.201.description}",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AccountHoldersDTO.class)
                    ),
                    headers = {
                            @Header(
                                    name = "Location",
                                    description = "${response.createJointAccountHolders.201.header.Location.description}",
                                    schema = @Schema(
                                            type = "string",
                                            format = "uri",
                                            example = "${response.createJointAccountHolders.201.header.Location.example}"
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "${response.createJointAccountHolders.400.description}"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "${response.createJointAccountHolders.500.description}"
            )
    })
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.CREATED)
    public HttpResponse<AccountHoldersDTO> createJointAccountHolder(
            @PathVariable("bank-account-id")
            UUID bankAccountId,

            @Body
            @NotEmpty List<AccountHolderRequest> accountHoldersRequest) {

        LOGGER.info("Received request to create joint account holders for Bank Account ID: {}", bankAccountId);

        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(bankAccountId, accountHoldersRequest);
        var accountHoldersDTO = createJointAccountHolderService.create(createJointAccountHoldersRequest);

        return HttpResponse.created(accountHoldersDTO, BankAccountURIBuilder.bankAccountURI(bankAccountId));
    }
}

