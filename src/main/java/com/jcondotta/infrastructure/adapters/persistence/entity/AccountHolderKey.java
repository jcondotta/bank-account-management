package com.jcondotta.infrastructure.adapters.persistence.entity;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public final class AccountHolderKey {

    public static final String ACCOUNT_HOLDER_PK_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String ACCOUNT_HOLDER_SK_TEMPLATE = "ACCOUNT_HOLDER#%s";

    private AccountHolderKey() {}

    public static String partitionKey(BankAccountId bankAccountId) {
        return ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId.value().toString());
    }

    public static String sortKey(AccountHolderId accountHolderId) {
        return ACCOUNT_HOLDER_SK_TEMPLATE.formatted(accountHolderId.value().toString());
    }
}