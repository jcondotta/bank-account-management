package com.jcondotta.domain.accountholder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcondotta.domain.accountholder.valueobjects.*;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;

public record AccountHolder(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountHolderTypeValue accountHolderType,
    CreatedAt createdAt
) {

    public static AccountHolder createPrimary(
        AccountHolderName name,
        PassportNumber passportNumber,
        DateOfBirth dateOfBirth,
        CreatedAt createdAt
    ) {
        return create(name, passportNumber, dateOfBirth, AccountHolderTypeValue.primary(), createdAt);
    }

    public static AccountHolder createJoint(
        AccountHolderName name,
        PassportNumber passportNumber,
        DateOfBirth dateOfBirth
    ) {
        return create(name, passportNumber, dateOfBirth, AccountHolderTypeValue.joint(), CreatedAt.now());
    }

    private static AccountHolder create(
        AccountHolderName name,
        PassportNumber passportNumber,
        DateOfBirth dateOfBirth,
        AccountHolderTypeValue type,
        CreatedAt createdAt
    ) {
        return new AccountHolder(
            AccountHolderId.newId(),
            name,
            passportNumber,
            dateOfBirth,
            type,
            createdAt
        );
    }

    @JsonIgnore
    public boolean isPrimary() {
        return accountHolderType.isPrimary();
    }

    @JsonIgnore
    public boolean isJoint() {
        return accountHolderType.isJoint();
    }
}


