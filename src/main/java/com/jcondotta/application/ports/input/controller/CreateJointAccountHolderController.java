package com.jcondotta.application.ports.input.controller;

import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderRequest;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("${api.v1.account-holders-path}")
public interface CreateJointAccountHolderController {

    @Operation(
        tags = {"Account holders"},
        summary = "Create joint account holders",
        description = "Add joint account holder to an existing bank account.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of joint account holders to be added",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CreateJointAccountHolderRequest.class)
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", description = "Joint account holders successfully added.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateJointAccountHolderResponse.class)),
            headers = {
                @Header(name = "Location", description = "URI of the newly created account holder",
                    schema = @Schema(type = "string", format = "uri",
                        example = "/api/v1/bank-accounts/01920bff-1338-7efd-ade6-e9128debe5d4/account-holders/c6a4a1b2-0f8c-41e3-a622-98d66de824a9"
                    )
                )
            }
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data. Check the provided joint account holder details.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
            {
                "title": "Bad Request",
                "status": 400,
                "timestamp": "2025-06-19T08:45:22.547943Z",
                "path": "/api/v1/bank-accounts/75d06872-45ac-43ff-944d-6e3aa6b371e2/account-holders",
                "errors": [
                    {
                        "field": "accountHolder.accountHolderName",
                        "messages": [
                            "account holder name must not be blank."
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
                        "path": "/api/v1/bank-accounts/01920bff-1338-7efd-ade6-e9128debe5d4/account-holders"
                    }
                    """
                )
            )
        )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CreateJointAccountHolderResponse> createJointAccountHolder(
            @Parameter(
                    description = "Unique identifier of the bank account",
                    required = true,
                    example = "01920bff-1338-7efd-ade6-e9128debe5d4"
            )
            @PathVariable("bank-account-id") UUID bankAccountId,
            @Valid @RequestBody CreateJointAccountHolderRequest request);
}