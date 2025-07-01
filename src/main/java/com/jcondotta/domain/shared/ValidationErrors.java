package com.jcondotta.domain.shared;

public interface ValidationErrors {

    String CREATED_AT_NOT_NULL = "common.createdAt.notNull";
    String CREATED_AT_IN_FUTURE = "common.createdAt.inFuture";

    interface AccountHolder {
        String ID_NOT_NULL = "accountHolder.accountHolderId.notNull";
        String NAME_BLANK = "accountHolder.accountHolderName.notBlank";
        String NAME_TOO_LONG = "accountHolder.accountHolderName.tooLong";
        String DOCUMENT_INVALID = "accountHolder.documentNumber.invalid";
        String DATE_OF_BIRTH_NOT_NULL = "accountHolder.dateOfBirth.notNull";
        String DATE_OF_BIRTH_IN_FUTURE = "accountHolder.dateOfBirth.inFuture";
        String ACCOUNT_HOLDER_TYPE_NOT_NULL = "accountHolder.accountHolderType.notNull";
    }

    interface BankAccount {
        String ID_NOT_NULL = "bankAccount.bankAccountId.notNull";
        String IBAN_NOT_BLANK = "bankAccount.iban.notBlank";
        String IBAN_INVALID_FORMAT = "bankAccount.iban.invalidFormat";
        String ACCOUNT_TYPE_NOT_NULL = "bankAccount.accountType.notNull";
        String STATUS_NOT_NULL = "bankAccount.status.notNull";
        String CURRENCY_NOT_NULL = "bankAccount.currency.notNull";
    }
}
