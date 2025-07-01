package com.jcondotta.application.ports.input.controller;

import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@RequestMapping("${api.v1.account-holder-path}")
public interface AccountHolderLookupController {

    @Operation(
        tags = {"Account holders"},
        summary = "Retrieve account holder details",
        description = "Retrieve details of an account holder using the bank account id and the account holder id."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account holder details successfully retrieved.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AccountHolderLookupResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account holder hasn't been found by the provided id.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                        "title": "Resource Not Found",
                        "status": 404,
                        "detail": "[bankAccountId=01920bff-1338-7efd-ade6-e9128debe5d4, accountHolderId=c6a4a1b2-0f8c-41e3-a622-98d66de824a9] No account holder has been found",
                        "timestamp": "2025-06-18T22:20:42.646279Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error. Unable to process the request at this time.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2025-06-19T08:54:33.599+00:00",
                        "status": 500,
                        "error": "Internal Server Error",
                        "path": "/api/v1/bank-accounts/01920bff-1338-7efd-ade6-e9128debe5d4/account-holders/c6a4a1b2-0f8c-41e3-a622-98d66de824a9"
                    }
                    """
                )
            )
        )
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AccountHolderLookupResponse> accountHolderLookup(
        @Parameter(
            description = "Unique identifier of the bank account",
            required = true,
            example = "01920bff-1338-7efd-ade6-e9128debe5d4"
        )
        @PathVariable("bank-account-id") UUID bankAccountId,

        @Parameter(
            description = "Unique identifier of the account holder",
            required = true,
            example = "c6a4a1b2-0f8c-41e3-a622-98d66de824a9"
        )
        @PathVariable("account-holder-id") UUID accountHolderId
    );
}