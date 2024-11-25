package com.jcondotta.web.controller.bank_account;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Singleton
public class BankAccountURIBuilder {

    public static final String BASE_PATH_API_V1_MAPPING = "/api/v1/bank-accounts";
    public static final String BANK_ACCOUNT_API_V1_MAPPING = BASE_PATH_API_V1_MAPPING + "/bank-account-id/{bank-account-id}";
    public static final String ACCOUNT_HOLDERS_API_V1_MAPPING = BANK_ACCOUNT_API_V1_MAPPING + "/account-holders";

    @Value("${api.v1.bankAccounts.basePath}")
    private String bankAccountsBasePath;

    @Value("${api.v1.bankAccounts.bankAccountPath}")
    private String bankAccountPath;

    @Value("${api.v1.bankAccounts.accountHoldersBasePath}")
    private String accountHoldersBasePath;

    public static URI bankAccountURI(@NotNull UUID bankAccountId) {
        return UriBuilder.of(BANK_ACCOUNT_API_V1_MAPPING)
                .expand(Map.of("bank-account-id", bankAccountId.toString()));
    }
}
