package com.jcondotta.application.usecase.createbankaccount.model;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.bankaccount.enums.Currency;

import java.util.Objects;

/**
 * Command representing the data required to create a bank account.
 *
 * @param accountType             the type of the bank account
 * @param currency                the currency of the bank account
 * @param createAccountHolderData the data for creating the primary account holder
 */
public record CreateBankAccountCommand(
    AccountType accountType,
    Currency currency,
    CreateAccountHolderData createAccountHolderData) {

    public CreateBankAccountCommand {
        Objects.requireNonNull(accountType, ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL);
        Objects.requireNonNull(currency, ValidationErrors.BankAccount.CURRENCY_NOT_NULL);
        Objects.requireNonNull(createAccountHolderData, ValidationErrors.AccountHolder.ACCOUNT_HOLDER_DATA_NOT_NULL);
    }

    public static CreateBankAccountCommand of(AccountType accountType, Currency currency, CreateAccountHolderData createAccountHolderData) {
        return new CreateBankAccountCommand(accountType, currency, createAccountHolderData);
    }
}
