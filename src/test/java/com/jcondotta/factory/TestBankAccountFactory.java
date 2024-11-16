package com.jcondotta.factory;

import com.jcondotta.domain.BankingEntity;
import net.datafaker.Faker;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestBankAccountFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockFactory.testClockFixedInstant;

    public static BankingEntity create(UUID bankAccountId, String iban, LocalDateTime dateOfOpening) {
        return BankingEntity.buildBankAccount(bankAccountId, iban, dateOfOpening);
    }

    public static BankingEntity create(UUID bankAccountId) {
        return create(bankAccountId, new Faker().finance().iban(), LocalDateTime.now(TEST_CLOCK_FIXED_INSTANT));
    }

    public static BankingEntity create() {
        return create(UUID.randomUUID());
    }
}