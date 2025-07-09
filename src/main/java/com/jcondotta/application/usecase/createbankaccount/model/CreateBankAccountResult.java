package com.jcondotta.application.usecase.createbankaccount.model;

import com.jcondotta.application.usecase.shared.model.BankAccountDetails;

import java.util.Objects;

/**
 * Result of creating a bank account.
 * Contains the details of the newly created bank account.
 *
 * @param bankAccount the details of the created bank account
 */
public record CreateBankAccountResult (BankAccountDetails bankAccount) {
    public CreateBankAccountResult {
        Objects.requireNonNull(bankAccount, "bankAccount must not be null");
    }

    public static CreateBankAccountResult of(BankAccountDetails bankAccount) {
        return new CreateBankAccountResult(bankAccount);
    }
}