package com.jcondotta.domain.bankaccount.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.domain.bankaccount.exceptions.EmptyAccountHolderListException;
import com.jcondotta.domain.bankaccount.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class BankAccount {

    public static final AccountStatus STATUS_ON_OPENING = AccountStatus.PENDING;
    public static final int MAX_JOINT_ACCOUNT_HOLDERS = 3;

    private final BankAccountId bankAccountId;
    private final AccountType accountType;
    private final Currency currency;
    private final Iban iban;
    private final AccountStatus status;
    private final CreatedAt createdAt;
    private final List<AccountHolder> accountHolders;

    @JsonCreator
    public BankAccount(
        @JsonProperty("bankAccountId") BankAccountId bankAccountId,
        @JsonProperty("accountType") AccountType accountType,
        @JsonProperty("currency") Currency currency,
        @JsonProperty("iban") Iban iban,
        @JsonProperty("status") AccountStatus status,
        @JsonProperty("createdAt") CreatedAt createdAt,
        @JsonProperty("accountHolders") List<AccountHolder> accountHolders
    ) {
        this.bankAccountId = requireNonNull(bankAccountId, ValidationErrors.BankAccount.ID_NOT_NULL);
        this.accountType = requireNonNull(accountType, ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL);
        this.currency = requireNonNull(currency, ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
        this.iban = requireNonNull(iban, ValidationErrors.BankAccount.IBAN_NOT_BLANK);
        this.status = requireNonNull(status, ValidationErrors.BankAccount.STATUS_NOT_NULL);
        this.createdAt = requireNonNull(createdAt, ValidationErrors.CREATED_AT_NOT_NULL);

        if (STATUS_ON_OPENING.isNot(status)) {
            throw new IllegalArgumentException("New account must start in PENDING status");
        }

        if (accountHolders == null || accountHolders.isEmpty()) {
            throw new EmptyAccountHolderListException();
        }

        if (accountHolders.stream().noneMatch(AccountHolder::isPrimary)) {
            throw new IllegalArgumentException("At least one primary account holder is required");
        }

        this.accountHolders = new ArrayList<>(accountHolders);
    }

    public BankAccountId bankAccountId() {
        return bankAccountId;
    }

    public AccountType accountType() {
        return accountType;
    }

    public Currency currency() {
        return currency;
    }

    public Iban iban() {
        return iban;
    }

    public AccountStatus status() {
        return status;
    }

    public CreatedAt createdAt() {
        return createdAt;
    }

    public List<AccountHolder> accountHolders() {
        return Collections.unmodifiableList(accountHolders);
    }

    public void addJointAccountHolder(AccountHolder accountHolder) {
        requireNonNull(accountHolder, "bankAccount.accountHolder.notNull");

        if (accountHolder.isPrimary()) {
            throw new IllegalArgumentException("Only joint account holders can be added");
        }

        var jointAccountHoldersCount = accountHolders.stream()
            .filter(Predicate.not(AccountHolder::isPrimary))
            .count();

        if (jointAccountHoldersCount >= MAX_JOINT_ACCOUNT_HOLDERS) {
            throw new MaxJointAccountHoldersExceededException(MAX_JOINT_ACCOUNT_HOLDERS, (int) jointAccountHoldersCount);
        }
        accountHolders.add(accountHolder);
    }

    public Optional<AccountHolder> findAccountHolder(AccountHolderId accountHolderId) {
        return accountHolders.stream()
            .filter(accountHolder -> accountHolder.accountHolderId().equals(accountHolderId))
            .findFirst();
    }

    @JsonIgnore
    public AccountHolder getPrimaryAccountHolder() {
        return accountHolders.stream()
            .filter(AccountHolder::isPrimary)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Primary account holder must exist"));
    }

    public List<AccountHolder> jointAccountHolders() {
        return accountHolders.stream()
            .filter(Predicate.not(AccountHolder::isPrimary))
            .toList();
    }

    public static BankAccount openBankAccount(AccountType accountType, Currency currency, Iban iban, AccountHolder primaryAccountHolder, Clock clock){
        return new BankAccount(BankAccountId.newId(), accountType, currency, iban, AccountStatus.PENDING, CreatedAt.now(clock), List.of(primaryAccountHolder));
    }
}