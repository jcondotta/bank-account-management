package com.jcondotta.service.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Serdeable
@Schema(description = "Request containing information for creating joint account holders.")
public record CreateJointAccountHoldersRequest(

        @Size(max = 2, message = "accountHolders.tooMany")
        @NotEmpty(message = "accountHolders.notEmpty")
        List<@Valid AccountHolderRequest> accountHolderRequests) {

    public CreateJointAccountHoldersRequest(AccountHolderRequest accountHolderRequest) {
        this(Objects.requireNonNullElse(List.of(accountHolderRequest), Collections.EMPTY_LIST));
    }
}
