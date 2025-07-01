package com.jcondotta.infrastructure.adapters.persistence.entity;

import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public final class BankAccountKey {

    public static final String BANK_ACCOUNT_PK_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String BANK_ACCOUNT_SK_TEMPLATE = "BANK_ACCOUNT#%s";

    private BankAccountKey() {}

    public static String partitionKey(BankAccountId bankAccountId) {
        return BANK_ACCOUNT_PK_TEMPLATE.formatted(bankAccountId.value().toString());
    }

    public static String sortKey(BankAccountId bankAccountId) {
        return BANK_ACCOUNT_SK_TEMPLATE.formatted(bankAccountId.value().toString());
    }
}