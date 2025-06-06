package com.jcondotta.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcondotta.domain.AccountHolderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "BankAccountDTO", description = "Represents a bank account entity with details.")
public class BankAccountDTO {

    @NotNull
    @Schema(description = "Unique identifier for the bank account.",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
            requiredMode = RequiredMode.REQUIRED)
    private UUID bankAccountId;

    @NotNull
    @Schema(description = "Account holders associated with this bank account.",
            requiredMode = RequiredMode.REQUIRED)
    private List<AccountHolderDTO> accountHolders;

    @NotBlank
    @Schema(description = "International Bank Account Number (IBAN) for the bank account.",
            example = "GB29NWBK60161331926819",
            maxLength = 34,
            requiredMode = RequiredMode.REQUIRED)
    private String iban;

    @NotNull
    @Schema(description = "Date and time when the bank account was opened.",
            example = "2023-08-23T14:55:00Z",
            requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime dateOfOpening;

    @JsonIgnore
    public Optional<AccountHolderDTO> getPrimaryAccountHolder() {
        return accountHolders.stream()
                .filter(dto -> AccountHolderType.PRIMARY.equals(dto.getAccountHolderType()))
                .findFirst();
    }
}
