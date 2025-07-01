package com.jcondotta.interfaces.rest.lookup;

import com.jcondotta.interfaces.rest.ValidatableRequest;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccountHolderLookupRequest(
    @NotNull
    BankAccountId bankAccountId,

    @NotNull
    AccountHolderId accountHolderId

) implements ValidatableRequest {

    public AccountHolderLookupRequest(UUID bankAccountId, UUID accountHolderId) {
        this(new BankAccountId(bankAccountId), new AccountHolderId(accountHolderId));
    }
}