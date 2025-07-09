package com.jcondotta.infrastructure.adapters.cache;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public final class BankAccountCacheKey {

    private static final String BANK_ACCOUNT_ID_TEMPLATE = "bankAccounts:%s";

    private BankAccountCacheKey() {}

    public static String of(BankAccountId bankAccountId) {
        return String.format(BANK_ACCOUNT_ID_TEMPLATE, bankAccountId.value().toString());
    }

    public static String of(BankAccount bankAccount) {
        return String.format(BANK_ACCOUNT_ID_TEMPLATE, bankAccount.bankAccountId().value().toString());
    }
}