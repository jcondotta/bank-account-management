package com.jcondotta.config;

import com.jcondotta.domain.AccountHolderType;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.BankingEntityMapper;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.request.CreateAccountHolderRequest;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

public class TestAccountHolderFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;

    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    protected static BankingEntity create(String accountHolderName, String passportNumber, LocalDate dateOfBirth,
                                          AccountHolderType accountHolderType, UUID bankAccountId) {
        var createAccountHolderRequest = new CreateAccountHolderRequest(
                accountHolderName, dateOfBirth, passportNumber
        );

        return BANKING_ENTITY_MAPPER.toAccountHolder(bankAccountId, createAccountHolderRequest, accountHolderType, TEST_CLOCK_FIXED_INSTANT);
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