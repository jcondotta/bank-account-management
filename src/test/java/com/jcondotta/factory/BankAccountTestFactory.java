package com.jcondotta.factory;

import com.jcondotta.domain.BankAccount;
import com.jcondotta.helper.TestAccountHolderRequest;
import net.datafaker.Faker;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class BankAccountTestFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = ClockTestFactory.testClockFixedInstant;

    public static BankAccount create(UUID bankAccountId, TestAccountHolderRequest testAccountHolderRequest) {
        var accountHolder = AccountHolderTestFactory.create(testAccountHolderRequest);

        return new BankAccount(bankAccountId, accountHolder, new Faker().finance().iban(), LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT));
    }

//    public static BankAccount create(TestAccountHolderRequest testAccountHolderRequest) {
//        return create(UUID.randomUUID(), testAccountHolderRequest);
//    }
}