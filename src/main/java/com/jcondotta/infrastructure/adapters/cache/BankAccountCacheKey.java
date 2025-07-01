package com.jcondotta.infrastructure.adapters.cache;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public final class BankAccountCacheKey {

    private static final String BANK_ACCOUNT_ID_TEMPLATE = "bankAccounts:%s";
    private static final String ACCOUNT_HOLDER_ID_TEMPLATE = "bankAccounts:%s:accountHolders:%s";
    private static final String ACCOUNT_HOLDERS_TEMPLATE = "bankAccounts:%s:accountHolders:*";

    private BankAccountCacheKey() {}

    public static String bankAccountIdKey(BankAccountId bankAccountId) {
        return String.format(BANK_ACCOUNT_ID_TEMPLATE, bankAccountId.value().toString());
    }

    public static String accountHoldersKey(BankAccountId bankAccountId) {
        return String.format(ACCOUNT_HOLDERS_TEMPLATE, bankAccountId.value().toString());
    }

    public static String accountHolderIdKey(BankAccountId bankAccountId, AccountHolderId accountHolderId) {
        return String.format(ACCOUNT_HOLDER_ID_TEMPLATE, bankAccountId, accountHolderId);
    }
}