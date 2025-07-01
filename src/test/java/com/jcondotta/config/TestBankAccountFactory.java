package com.jcondotta.config;

import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import net.datafaker.Faker;

import java.time.Clock;
import java.util.UUID;

public class TestBankAccountFactory {

    private static final Clock TEST_CLOCK_FIXED_INSTANT = TestClockConfig.testClockFixedInstant;

//    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    public static BankingEntity create(UUID bankAccountId, String iban, Clock currentClock) {
        //return BANKING_ENTITY_MAPPER.toBankAccountEntity(bankAccountId, AccountType.CHECKING, Currency.EUR, iban, BankAccountStatus.PENDING, currentClock);
        return null;
    }

    public static BankingEntity create(UUID bankAccountId) {
        return create(bankAccountId, new Faker().finance().iban(), TEST_CLOCK_FIXED_INSTANT);
    }

    public static BankingEntity create() {
        return create(UUID.randomUUID());
    }
}