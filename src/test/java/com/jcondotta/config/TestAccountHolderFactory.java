package com.jcondotta.config;

import com.jcondotta.application.dto.AccountHolderDetailsRequest;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.domain.model.AccountHolderType;
import com.jcondotta.domain.model.BankingEntity;
import com.jcondotta.helper.TestAccountHolderRequest;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

public class TestAccountHolderFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;

    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    protected static BankingEntity create(String accountHolderName, String passportNumber, LocalDate dateOfBirth,
                                          AccountHolderType accountHolderType, UUID bankAccountId) {
        var accountHolderId = UUID.randomUUID();
        var createAccountHolderRequest = new AccountHolderDetailsRequest(
                accountHolderName, dateOfBirth, passportNumber
        );

        return BANKING_ENTITY_MAPPER.toAccountHolderEntity(
            accountHolderId,
            bankAccountId,
            createAccountHolderRequest,
            accountHolderType,
            TEST_CLOCK_FIXED_INSTANT
        );
    }

    public static BankingEntity create(TestAccountHolderRequest testAccountHolderRequest,
                                       AccountHolderType accountHolderType, UUID bankAccountId) {
        return create(
                testAccountHolderRequest.getAccountHolderName(),
                testAccountHolderRequest.getPassportNumber(),
                testAccountHolderRequest.getDateOfBirth(),
                accountHolderType,
                bankAccountId
        );
    }

    public static BankingEntity createPrimaryAccountHolder(TestAccountHolderRequest testAccountHolderRequest, UUID bankAccountId) {
        return create(testAccountHolderRequest, AccountHolderType.PRIMARY, bankAccountId);
    }

    public static BankingEntity createJointAccountHolder(TestAccountHolderRequest testAccountHolderRequest, UUID bankAccountId) {
        return create(testAccountHolderRequest, AccountHolderType.JOINT, bankAccountId);
    }
}