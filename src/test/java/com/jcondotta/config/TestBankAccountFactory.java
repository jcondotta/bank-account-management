package com.jcondotta.config;

import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.bankaccount.enums.Currency;

import java.time.Clock;
import java.util.List;

public class TestBankAccountFactory {

    public static BankAccount createWithPrimaryAccountHolder(AccountType accountType, Currency currency, AccountHolder accountHolder, Clock clock) {
        return BankAccount.openBankAccount(accountType, currency, Iban.generate(), accountHolder, clock);
    }

    public static BankAccount createWithPrimaryAccountHolder(AccountType accountType, Currency currency, AccountHolder accountHolder) {
        return createWithPrimaryAccountHolder(accountType, currency, accountHolder, Clock.systemDefaultZone());
    }

    public static BankAccount createWithPrimaryAccountHolder(AccountHolder accountHolder, Clock clock) {
        return createWithPrimaryAccountHolder(AccountType.CHECKING, Currency.EUR, accountHolder, clock);
    }

    public static BankAccount createWithPrimaryAccountHolder(AccountHolder accountHolder) {
        return createWithPrimaryAccountHolder(accountHolder, Clock.systemDefaultZone());
    }

    public static BankAccount createWithPrimaryAccountHolder(AccountType accountType, Currency currency, String accountHolderName, Clock clock) {
        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryWithName(accountHolderName, clock);
        return createWithPrimaryAccountHolder(accountType, currency, primaryAccountHolder, clock);
    }

    public static BankAccount createWithPrimaryAccountHolder(AccountType accountType, Currency currency, String accountHolderName) {
        return createWithPrimaryAccountHolder(accountType, currency, accountHolderName, Clock.systemDefaultZone());
    }

    public static BankAccount createWithPrimaryAccountHolder(String accountHolderName, Clock clock) {
        return createWithPrimaryAccountHolder(AccountType.CHECKING, Currency.EUR, accountHolderName, clock);
    }

    public static BankAccount createWithPrimaryAccountHolder(String accountHolderName) {
        return createWithPrimaryAccountHolder(accountHolderName, Clock.systemDefaultZone());
    }

    public static BankAccount createWithPrimaryAndJointAccountHolders(AccountHolder primaryAccountHolder, List<AccountHolder> jointAccountHolders, Clock clock) {
        var bankAccount = createWithPrimaryAccountHolder(primaryAccountHolder, clock);
        jointAccountHolders.forEach(bankAccount::addJointAccountHolder);

        return bankAccount;
    }

    public static BankAccount createWithPrimaryAndJointAccountHolders(AccountHolder primaryAccountHolder, List<AccountHolder> jointAccountHolders) {
        return createWithPrimaryAndJointAccountHolders(primaryAccountHolder, jointAccountHolders, Clock.systemDefaultZone());
    }

    public static BankAccount createWithPrimaryAndJointAccountHolders(AccountHolder primaryAccountHolder, AccountHolder jointAccountHolder, Clock clock) {
        return createWithPrimaryAndJointAccountHolders(primaryAccountHolder, List.of(jointAccountHolder), clock);
    }

    public static BankAccount createWithPrimaryAndJointAccountHolders(AccountHolder primaryAccountHolder, AccountHolder jointAccountHolder) {
        return createWithPrimaryAndJointAccountHolders(primaryAccountHolder, jointAccountHolder, Clock.systemDefaultZone());
    }
}