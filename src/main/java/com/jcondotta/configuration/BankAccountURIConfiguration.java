package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.uri.UriBuilder;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@ConfigurationProperties("api.v1")
public record BankAccountURIConfiguration(String rootPath, String bankAccountPath, String accountHoldersPath)
{
    public static final String BASE_PATH_API_V1_MAPPING = "/api/v1/bank-accounts";
    public static final String BANK_ACCOUNT_API_V1_MAPPING = BASE_PATH_API_V1_MAPPING + "/bank-account-id/{bank-account-id}";
    public static final String ACCOUNT_HOLDERS_API_V1_MAPPING = BANK_ACCOUNT_API_V1_MAPPING + "/account-holders";

    public static URI bankAccountURI(@NotNull UUID bankAccountId) {
        return UriBuilder.of(BANK_ACCOUNT_API_V1_MAPPING)
                .expand(Map.of("bank-account-id", bankAccountId.toString()));
    }
}
