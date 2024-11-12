package com.jcondotta.factory;

import com.jcondotta.domain.BankAccount;
import net.datafaker.Faker;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class BankAccountTestFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = ClockTestFactory.testClockFixedInstant;

    public static BankAccount create() {
        return new BankAccount(UUID.randomUUID(), new Faker().finance().iban(), LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT));
    }
}