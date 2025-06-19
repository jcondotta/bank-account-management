package com.jcondotta.application;

import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.domain.value_object.BankAccountId;

/**
 * Use case interface for looking up bank accounts.
 * This interface defines a method to retrieve details of a bank account by its ID.
 */
public interface BankAccountLookupUseCase {

    /**
     * Looks up a bank account by its ID.
     *
     * @param bankAccountId the ID of the bank account to look up
     * @return a response containing details of the bank account
     */
    BankAccountLookupResponse lookup(BankAccountId bankAccountId);
}