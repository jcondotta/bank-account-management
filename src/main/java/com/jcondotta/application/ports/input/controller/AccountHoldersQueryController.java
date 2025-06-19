package com.jcondotta.application.ports.input.controller;

import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("${api.v1.account-holders-path}")
public interface AccountHoldersQueryController {

    @Operation(
        tags = {"Account holders"},
        summary = "${accountHoldersQuery.operation.summary}",
        description = "${accountHoldersQuery.operation.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "${accountHoldersQuery.response.200.description}",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AccountHoldersQueryResponse.class)
            )
        ),
//        @ApiResponse(
//            responseCode = "404",
//            description = "${accountHolderLookup.response.404.description}",
//            content = @Content(
//                mediaType = MediaType.APPLICATION_JSON_VALUE,
//                schema = @Schema(implementation = ProblemDetail.class),
//                examples = @ExampleObject(
//                    value = """
//                        {
//                            "title": "Resource Not Found",
//                            "status": 404,
//                            "detail": "[bankAccountId=01920bff-1338-7efd-ade6-e9128debe5d4, accountHolderId=c6a4a1b2-0f8c-41e3-a622-98d66de824a9] No account holder has been found",
//                            "timestamp": "2025-06-18T22:20:42.646279Z"
//                        }
//                        """
//                )
//            )
//        )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AccountHoldersQueryResponse> findBankAccount(
        @Parameter(
        description = "${accountHoldersQuery.parameter.bankAccountId.description}",
        required = true,
        example = "01920bff-1338-7efd-ade6-e9128debe5d4")

        @PathVariable("bank-account-id") UUID bankAccountId);
}