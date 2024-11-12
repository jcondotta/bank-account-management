package com.jcondotta.factory;

import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.helper.TestAccountHolderRequest;

import java.time.LocalDate;
import java.util.UUID;

public class AccountHolderTestFactory {

    protected static AccountHolder create(UUID bankAccountId, UUID accountHolderId, String accountHolderName, String passportNumber, LocalDate dateOfBirth, AccountHolderType accountHolderType) {
        return new AccountHolder(bankAccountId, accountHolderId, accountHolderName, passportNumber, dateOfBirth, accountHolderType);
    }

    public static AccountHolder create(UUID bankAccountId, UUID accountHolderId, TestAccountHolderRequest testAccountHolderRequest, AccountHolderType accountHolderType) {
        return create(bankAccountId, accountHolderId, testAccountHolderRequest.getAccountHolderName(), testAccountHolderRequest.getPassportNumber(), testAccountHolderRequest.getDateOfBirth(), accountHolderType);
    }

    public static AccountHolder createPrimaryAccountHolder(UUID bankAccountId, TestAccountHolderRequest testAccountHolderRequest) {
        return create(bankAccountId, UUID.randomUUID(), testAccountHolderRequest, AccountHolderType.PRIMARY);
    }
}