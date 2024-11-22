package com.jcondotta.web.controller.bank_account;

import io.micronaut.http.uri.UriBuilder;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public interface BankAccountURIBuilder {

    String BASE_PATH_API_V1_MAPPING = "/api/v1/bank-accounts";
    String BANK_ACCOUNT_API_V1_MAPPING = BASE_PATH_API_V1_MAPPING + "/bank-account-id/{bank-account-id}";
    String ACCOUNT_HOLDERS_API_V1_MAPPING = BANK_ACCOUNT_API_V1_MAPPING + "/account-holders";

    static URI bankAccountURI(@NotNull UUID bankAccountId) {
        return UriBuilder.of(BANK_ACCOUNT_API_V1_MAPPING)
                .expand(Map.of("bank-account-id", bankAccountId.toString()));
    }
}
