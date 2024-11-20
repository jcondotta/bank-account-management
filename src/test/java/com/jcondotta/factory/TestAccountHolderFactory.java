package com.jcondotta.factory;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.helper.TestAccountHolderRequest;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestAccountHolderFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockFactory.testClockFixedInstant;

    protected static BankingEntity create(UUID accountHolderId, String accountHolderName, String passportNumber, LocalDate dateOfBirth,
                                          AccountHolderType accountHolderType, UUID bankAccountId) {
        return BankingEntity.buildAccountHolder(
                accountHolderId,
                accountHolderName,
                passportNumber,
                dateOfBirth,
                accountHolderType,
                LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT),
                bankAccountId
        );
    }

    public static BankingEntity create(UUID accountHolderId, TestAccountHolderRequest testAccountHolderRequest,
                                       AccountHolderType accountHolderType, UUID bankAccountId) {
        return create(
                accountHolderId,
                testAccountHolderRequest.getAccountHolderName(),
                testAccountHolderRequest.getPassportNumber(),
                testAccountHolderRequest.getDateOfBirth(),
                accountHolderType, bankAccountId
        );
    }

    public static BankingEntity createPrimaryAccountHolder(TestAccountHolderRequest testAccountHolderRequest, UUID bankAccountId) {
        return create(UUID.randomUUID(), testAccountHolderRequest, AccountHolderType.PRIMARY, bankAccountId);
    }

    public static BankingEntity createJointAccountHolder(TestAccountHolderRequest testAccountHolderRequest, UUID bankAccountId) {
        return create(UUID.randomUUID(), testAccountHolderRequest, AccountHolderType.JOINT, bankAccountId);
    }
}