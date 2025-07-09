package com.jcondotta.domain.accountholder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;

import java.time.Clock;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public record AccountHolder(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountHolderType accountHolderType,
    CreatedAt createdAt
) {
    public AccountHolder {
        requireNonNull(accountHolderId, ValidationErrors.AccountHolder.ID_NOT_NULL);
        requireNonNull(accountHolderName, ValidationErrors.AccountHolder.NAME_NOT_NULL);
        requireNonNull(passportNumber, ValidationErrors.AccountHolder.PASSPORT_NUMBER_NOT_NULL);
        requireNonNull(dateOfBirth, ValidationErrors.AccountHolder.DATE_OF_BIRTH_NOT_NULL);
        requireNonNull(accountHolderType, ValidationErrors.AccountHolder.ACCOUNT_HOLDER_TYPE_NOT_NULL);
        requireNonNull(createdAt, ValidationErrors.CREATED_AT_NOT_NULL);
    }

    public static AccountHolder createPrimary(CreateAccountHolderData accountHolderData, Clock clock) {
        return createPrimary(accountHolderData.accountHolderName(), accountHolderData.passportNumber(), accountHolderData.dateOfBirth(), clock);
    }

    public static AccountHolder createPrimary(CreateAccountHolderData accountHolderData) {
        return createPrimary(accountHolderData.accountHolderName(), accountHolderData.passportNumber(), accountHolderData.dateOfBirth());
    }

    public static AccountHolder createPrimary(String name, String passportNumber, LocalDate dateOfBirth, Clock clock) {
        return create(name, passportNumber, dateOfBirth, AccountHolderType.PRIMARY, clock);
    }

    public static AccountHolder createPrimary(String name, String passportNumber, LocalDate dateOfBirth) {
        return createPrimary(name, passportNumber, dateOfBirth, Clock.systemDefaultZone());
    }

    public static AccountHolder createPrimary(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, Clock clock) {
        return create(name, passportNumber, dateOfBirth, AccountHolderType.PRIMARY, CreatedAt.now(clock));
    }

    public static AccountHolder createPrimary(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth) {
        return create(name, passportNumber, dateOfBirth, AccountHolderType.PRIMARY, CreatedAt.now(Clock.systemDefaultZone()));
    }

    public static AccountHolder createJoint(CreateAccountHolderData accountHolderData, Clock clock) {
        return createJoint(accountHolderData.accountHolderName(), accountHolderData.passportNumber(), accountHolderData.dateOfBirth(), clock);
    }

    public static AccountHolder createJoint(CreateAccountHolderData accountHolderData) {
        return createJoint(accountHolderData.accountHolderName(), accountHolderData.passportNumber(), accountHolderData.dateOfBirth());
    }

    public static AccountHolder createJoint(String name, String passportNumber, LocalDate dateOfBirth, Clock clock) {
        return create(name, passportNumber, dateOfBirth, AccountHolderType.JOINT, clock);
    }

    public static AccountHolder createJoint(String name, String passportNumber, LocalDate dateOfBirth) {
        return createJoint(name, passportNumber, dateOfBirth, Clock.systemDefaultZone());
    }

    public static AccountHolder createJoint(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, Clock clock) {
        return create(name, passportNumber, dateOfBirth, AccountHolderType.JOINT, CreatedAt.now(clock));
    }

    public static AccountHolder createJoint(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth) {
        return create(name, passportNumber, dateOfBirth, AccountHolderType.JOINT, CreatedAt.now(Clock.systemDefaultZone()));
    }

    public static AccountHolder create(String name, String passportNumber, LocalDate dateOfBirth, AccountHolderType type, Clock clock) {
        return create(
            AccountHolderName.of(name),
            PassportNumber.of(passportNumber),
            DateOfBirth.of(dateOfBirth),
            type,
            CreatedAt.now(clock)
        );
    }

    public static AccountHolder create(CreateAccountHolderData createAccountHolderData, AccountHolderType type, Clock clock) {
        return create(
            createAccountHolderData.accountHolderName(),
            createAccountHolderData.passportNumber(),
            createAccountHolderData.dateOfBirth(),
            type,
            CreatedAt.now(clock)
        );
    }

    public static AccountHolder create(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, AccountHolderType type, CreatedAt createdAt) {
        return new AccountHolder(
            AccountHolderId.newId(),
            name,
            passportNumber,
            dateOfBirth,
            type,
            createdAt
        );
    }

    @JsonIgnore
    public boolean isPrimary() {
        return accountHolderType.isPrimary();
    }

    @JsonIgnore
    public boolean isJoint() {
        return accountHolderType.isJoint();
    }
}
