package com.jcondotta.domain.bankaccount.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.exceptions.AccountHolderAlreadyExistsException;
import com.jcondotta.domain.bankaccount.exceptions.EmptyAccountHolderListException;
import com.jcondotta.domain.bankaccount.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.domain.bankaccount.exceptions.RemovePrimaryAccountHolderException;
import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;

import java.util.*;
import java.util.function.Predicate;

public final class BankAccount {

    private static final int MAX_JOINT_ACCOUNT_HOLDERS = 3;

    private final BankAccountId bankAccountId;
    private final AccountTypeValue accountType;
    private final CurrencyValue currency;
    private final Iban iban;
    private final AccountStatusValue status;
    private final CreatedAt createdAt;
    private final List<AccountHolder> accountHolders;

    @JsonCreator
    public BankAccount(
        @JsonProperty("bankAccountId") BankAccountId bankAccountId,
        @JsonProperty("accountType") AccountTypeValue accountType,
        @JsonProperty("currency") CurrencyValue currency,
        @JsonProperty("iban") Iban iban,
        @JsonProperty("status") AccountStatusValue status,
        @JsonProperty("createdAt") CreatedAt createdAt,
        @JsonProperty("accountHolders") List<AccountHolder> accountHolders
    ) {
        this.bankAccountId = Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
        this.accountType = Objects.requireNonNull(accountType, "accountType must not be null");
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
        this.iban = Objects.requireNonNull(iban, "iban must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");

        if (accountHolders == null || accountHolders.isEmpty()) {
            throw new EmptyAccountHolderListException();
        }

        if (accountHolders.stream().noneMatch(AccountHolder::isPrimary)) {
            throw new IllegalArgumentException("accountHolders must not contain null elements");
        }

        this.accountHolders = new ArrayList<>(accountHolders);
    }

    public BankAccountId bankAccountId() {
        return bankAccountId;
    }

    public AccountTypeValue accountType() {
        return accountType;
    }

    public CurrencyValue currency() {
        return currency;
    }

    public Iban iban() {
        return iban;
    }

    public AccountStatusValue status() {
        return status;
    }

    public CreatedAt createdAt() {
        return createdAt;
    }

    public List<AccountHolder> accountHolders() {
        return Collections.unmodifiableList(accountHolders);
    }

    public void addJointAccountHolder(AccountHolder accountHolder) {
        Objects.requireNonNull(accountHolder, "bankAccount.accountHolder.notNull");

        if (accountHolders.contains(accountHolder)) {
            throw new AccountHolderAlreadyExistsException(bankAccountId);
        }

        var jointAccountHoldersCount = accountHolders.stream()
            .filter(Predicate.not(AccountHolder::isPrimary))
            .count();

        if (jointAccountHoldersCount >= MAX_JOINT_ACCOUNT_HOLDERS) {
            throw new MaxJointAccountHoldersExceededException(MAX_JOINT_ACCOUNT_HOLDERS, (int) jointAccountHoldersCount);
        }
        accountHolders.add(accountHolder);
    }

    public void removeJointAccountHolder(AccountHolder accountHolder) {
        Objects.requireNonNull(accountHolder, "bankAccount.accountHolder.notNull");

        if (!accountHolder.isPrimary()) {
            accountHolders.remove(accountHolder);
        }
    }

    public Optional<AccountHolder> findAccountHolder(AccountHolderId accountHolderId) {
        return accountHolders.stream()
            .filter(accountHolder -> accountHolder.accountHolderId().equals(accountHolderId))
            .findFirst();
    }

    public void removeJointAccountHolder(AccountHolderId accountHolderId) {
        findAccountHolder(accountHolderId)
            .ifPresent(accountHolder -> {
                if(accountHolder.isPrimary()) {
                    throw new RemovePrimaryAccountHolderException();
                }
                accountHolders.remove(accountHolder);
            });
    }
}