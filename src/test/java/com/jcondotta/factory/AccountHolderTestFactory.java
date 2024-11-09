package com.jcondotta.factory;

import com.jcondotta.domain.AccountHolder;
import com.jcondotta.helper.TestAccountHolderRequest;

import java.time.LocalDate;
import java.util.UUID;

public class AccountHolderTestFactory {

    public static AccountHolder create(UUID accountHolderId, String accountHolderName, String passportNumber, LocalDate dateOfBirth) {
        return new AccountHolder(accountHolderId, accountHolderName, passportNumber, dateOfBirth);
    }

    public static AccountHolder create(UUID accountHolderId, TestAccountHolderRequest testAccountHolderRequest) {
        return create(accountHolderId, testAccountHolderRequest.getAccountHolderName(), testAccountHolderRequest.getPassportNumber(), testAccountHolderRequest.getDateOfBirth());
    }

    public static AccountHolder create(TestAccountHolderRequest testAccountHolderRequest) {
        return create(UUID.randomUUID(), testAccountHolderRequest.getAccountHolderName(), testAccountHolderRequest.getPassportNumber(), testAccountHolderRequest.getDateOfBirth());
    }
}