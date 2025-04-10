package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.uri.UriBuilder;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@ConfigurationProperties("api.v1")
public record BankAccountURIConfiguration(String rootPath, String bankAccountPath, String bankAccountIbanPath, String accountHoldersPath)

{
    public URI bankAccountURI(@NotNull UUID bankAccountId) {
        return UriBuilder.of(bankAccountPath)
                .expand(Map.of("bank-account-id", bankAccountId.toString()));
    }

    public URI bankAccountByIbanURI(@NotNull String bankAccountIban) {
        return UriBuilder.of(bankAccountPath)
                .expand(Map.of("bankAccountIban", bankAccountIban));
    }
}
