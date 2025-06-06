package com.jcondotta.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.UUID;

@Validated
@ConfigurationProperties(prefix = "api.v1")
public record BankAccountURIConfiguration(
        @NotBlank String rootPath,
        @NotBlank String bankAccountPath,
        @NotBlank String bankAccountIbanPath,
        @NotBlank String accountHoldersPath
) {

    public URI bankAccountURI(UUID bankAccountId) {
        String expanded = bankAccountPath.replace("{bank-account-id}", bankAccountId.toString());
        return URI.create(expanded);
    }

    public URI bankAccountByIbanURI(String bankAccountIban) {
        String expanded = bankAccountIbanPath.replace("{iban}", bankAccountIban);
        return URI.create(expanded);
    }
}
