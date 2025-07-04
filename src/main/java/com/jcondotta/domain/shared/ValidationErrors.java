package com.jcondotta.domain.shared;

public interface ValidationErrors {

    String CREATED_AT_NOT_NULL = "common.createdAt.notNull";
    String CREATED_AT_IN_FUTURE = "common.createdAt.inFuture";

    interface AccountHolder {
        String ID_NOT_NULL = "accountHolder.accountHolderId.notNull";
        String NAME_NOT_BLANK = "accountHolder.accountHolderName.notBlank";
        String NAME_TOO_LONG = "accountHolder.accountHolderName.tooLong";
        String PASSPORT_NUMBER_INVALID_LENGTH = "accountHolder.passportNumber.invalidLength";
        String PASSPORT_NUMBER_NOT_NULL = "accountHolder.passportNumber.notNull";
        String DATE_OF_BIRTH_NOT_NULL = "accountHolder.dateOfBirth.notNull";
        String DATE_OF_BIRTH_NOT_IN_PAST = "accountHolder.dateOfBirth.notInPast";
        String ACCOUNT_HOLDER_TYPE_NOT_NULL = "accountHolder.accountHolderType.notNull";
    }

    interface BankAccount {
        String ID_NOT_NULL = "bankAccount.bankAccountId.notNull";
        String IBAN_NOT_BLANK = "bankAccountDetails.iban.notBlank";
        String IBAN_INVALID_FORMAT = "bankAccountDetails.iban.invalidFormat";
        String ACCOUNT_TYPE_NOT_NULL = "bankAccount.accountType.notNull";
        String STATUS_NOT_NULL = "bankAccount.status.notNull";
        String CURRENCY_NOT_NULL = "bankAccount.currency.notNull";
        String CURRENCY_INVALID = "bankAccount.currency.invalid";
        String ACCOUNT_HOLDER_NOT_NULL = "bankAccount.accountHolder.notNull";
    }
}
