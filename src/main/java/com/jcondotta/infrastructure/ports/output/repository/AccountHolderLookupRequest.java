package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.application.dto.ValidatableRequest;
import com.jcondotta.domain.value_object.AccountHolderId;
import com.jcondotta.domain.value_object.BankAccountId;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountHolderLookupRequest(
    @NotNull
    BankAccountId bankAccountId,

    @NotNull
    AccountHolderId accountHolderId

) implements ValidatableRequest {

    public AccountHolderLookupRequest(UUID bankAccountUuid, UUID accountHolderId) {
        this(new BankAccountId(bankAccountUuid), new AccountHolderId(accountHolderId));
    }
}
