package com.jcondotta.cache;

import com.jcondotta.domain.value_object.AccountHolderId;
import com.jcondotta.domain.value_object.BankAccountId;

import java.util.UUID;

public class BankAccountCacheKeys {

    private static final String BANK_ACCOUNT_ID_TEMPLATE = "bankAccount:bankAccountId:%s";
    private static final String ACCOUNT_HOLDER_ID_TEMPLATE = "accountHolder:accountHolderId:%s";

    public static String bankAccountIdKey(UUID bankAccountId) {
        return String.format(BANK_ACCOUNT_ID_TEMPLATE, bankAccountId);
    }

    public static String bankAccountIdKey(BankAccountId bankAccountId) {
        return String.format(BANK_ACCOUNT_ID_TEMPLATE, bankAccountId.value());
    }

    public static String accountHolderIdKey(UUID accountHolderId) {
        return String.format(ACCOUNT_HOLDER_ID_TEMPLATE, accountHolderId);
    }

    public static String accountHolderIdKey(AccountHolderId accountHolderId) {
        return String.format(ACCOUNT_HOLDER_ID_TEMPLATE, accountHolderId.value());
    }
}