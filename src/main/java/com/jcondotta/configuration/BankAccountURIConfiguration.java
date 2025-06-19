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
        @NotBlank String accountHoldersPath,
        @NotBlank String accountHolderPath
) {

    public URI bankAccountURI(UUID bankAccountId) {
        String expanded = bankAccountPath.replace("{bank-account-id}", bankAccountId.toString());
        return URI.create(expanded);
    }

    public URI accountHolderURI(UUID bankAccountId, UUID accountHolderId) {
        String expanded = accountHolderPath
            .replace("{bank-account-id}", bankAccountId.toString())
            .replace("{account-holder-id}", accountHolderId.toString());
        return URI.create(expanded);
    }
}
