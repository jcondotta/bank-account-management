package com.jcondotta.application.ports.input.controller;

import com.jcondotta.interfaces.rest.lookupbankaccount.model.BankAccountLookupRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface BankAccountLookupController {

    @Operation(
            tags = {"Bank accounts"},
            summary = "Retrieve bank account details",
            description = "Retrieve details of a bank account using the bank account id."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Bank account details successfully retrieved.",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = BankAccountLookupRestResponse.class)
                )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bank account hasn't been found by the provided id.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
            {
                "title": "Resource Not Found",
                "status": 404,
                "detail": "[bankAccountId=01920bff-1338-7efd-ade6-e9128debe5d4] No bank account has been found",
                "timestamp": "2025-06-19T08:38:34.169054Z"
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
                        "path": "/api/v1/bank-accounts/01920bff-1338-7efd-ade6-e9128debe5d4"
                    }
                    """
                )
            )
        )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BankAccountLookupRestResponse> findBankAccount(
        @Parameter(
                description = "Unique identifier of the bank account",
                required = true,
                example = "01920bff-1338-7efd-ade6-e9128debe5d4"
        )
        @PathVariable(value = "bank-account-id") UUID bankAccountId);
}