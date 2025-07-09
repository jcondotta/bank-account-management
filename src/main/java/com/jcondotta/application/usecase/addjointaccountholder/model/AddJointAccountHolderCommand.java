package com.jcondotta.application.usecase.addjointaccountholder.model;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.ValidationErrors;

import java.util.Objects;

/**
 * Command representing the data required to add a joint account holder.
 *
 * @param bankAccountId           the id of the bank account
 * @param createAccountHolderData the data for creating the new account holder
 */
public record AddJointAccountHolderCommand(BankAccountId bankAccountId, CreateAccountHolderData createAccountHolderData) {

    public AddJointAccountHolderCommand {
        Objects.requireNonNull(bankAccountId, ValidationErrors.BankAccount.ID_NOT_NULL);
        Objects.requireNonNull(createAccountHolderData, ValidationErrors.AccountHolder.ACCOUNT_HOLDER_DATA_NOT_NULL);
    }

    public static AddJointAccountHolderCommand of(BankAccountId bankAccountId, CreateAccountHolderData createAccountHolderData) {
        return new AddJointAccountHolderCommand(bankAccountId, createAccountHolderData);
    }
}
