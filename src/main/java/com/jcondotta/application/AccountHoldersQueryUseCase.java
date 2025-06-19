package com.jcondotta.application;

import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;

/**
 * Use case interface for querying account holders of a bank account.
 * This interface defines a method to retrieve the account holders associated with a given bank account.
 */
public interface AccountHoldersQueryUseCase {

    /**
     * Queries the account holders for the specified bank account.
     *
     * @param bankAccountId the ID of the bank account
     * @return a response containing details of the account holders associated with the bank account
     */
    AccountHoldersQueryResponse query(BankAccountId bankAccountId);
}