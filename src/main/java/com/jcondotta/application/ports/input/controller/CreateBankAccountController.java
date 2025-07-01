package com.jcondotta.application.ports.input.controller;

import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountRequest;
import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${api.v1.root-path}")
@Tag(name = "Bank accounts", description = "Operations related to bank accounts")
public interface CreateBankAccountController {

    @Operation(
            summary = "Create a new bank account",
            description = "Creates a new bank account with the provided account holder information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Bank account and primary account holder request details",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateBankAccountRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Bank account successfully created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = CreateBankAccountResponse.class)
                    ),
                    headers = {
                            @Header(
                                name = "Location",
                                description = "URI of the newly created bank account",
                                schema = @Schema(type = "string", format = "uri",
                                        example = "/api/v1/bank-accounts/01920bff-1338-7efd-ade6-e9128debe5d4"
                                )
                            )
                    }
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Request validation failed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                        value = """
                        {
                             "title": "Bad Request",
                             "status": 400,
                             "timestamp": "2025-06-19T08:14:04.595927Z",
                             "path": "/api/v1/bank-accounts",
                             "errors": [
                                 {
                                     "field": "accountType",
                                     "messages": [
                                         "The account type must be specified."
                                     ]
                                 },
                                 {
                                     "field": "accountHolder.dateOfBirth",
                                     "messages": [
                                         "Date of birth must be in the past."
                                     ]
                                 }
                             ]
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
                        "path": "/api/v1/bank-accounts"
                    }
                    """
                    )
                )
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CreateBankAccountResponse> createBankAccount(@Valid @RequestBody CreateBankAccountRequest request);
}