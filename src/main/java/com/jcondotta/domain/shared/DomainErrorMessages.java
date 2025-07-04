package com.jcondotta.domain.shared;

public interface DomainErrorMessages {

    String BANK_ACCOUNT_NOT_FOUND_MESSAGE = "bankAccount.notFound";
    String MAX_ACCOUNT_HOLDERS_LIMIT_REACHED = "bankAccount.accountHolders.tooMany";
    String ACCOUNT_HOLDER_NOT_FOUND = "accountHolder.notFound";
    String ACCOUNT_HOLDER_ALREADY_EXISTS = "bankAccountDetails.accountHolder.alreadyExists";
    String PRIMARY_ACCOUNT_HOLDERS_MISSING = "bankAccountDetails.primaryAccountHolder.missing";
}