package com.jcondotta.config;

import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.Clock;
import java.time.LocalDate;

public class TestAccountHolderFactory {

    private static AccountHolder create(String accountHolderName, AccountHolderType accountHolderType, Clock clock) {
        return AccountHolder.create(accountHolderName,
            RandomStringUtils.randomAlphanumeric(8).toUpperCase(),
            LocalDate.now(clock),
            accountHolderType,
            clock);
    }

    private static AccountHolder create(String accountHolderName, AccountHolderType accountHolderType) {
        return create(accountHolderName, accountHolderType, Clock.systemDefaultZone());
    }

    public static AccountHolder createPrimaryWithName(String accountHolderName, Clock clock) {
        return create(accountHolderName, AccountHolderType.PRIMARY, clock);
    }

    public static AccountHolder createPrimaryWithName(String accountHolderName) {
        return create(accountHolderName, AccountHolderType.PRIMARY);
    }

    public static AccountHolder createJointWithName(String accountHolderName, Clock clock) {
        return create(accountHolderName, AccountHolderType.JOINT, clock);
    }

    public static AccountHolder createJointWithName(String accountHolderName) {
        return create(accountHolderName, AccountHolderType.JOINT);
    }
}