package com.jcondotta.application.usecase.lookupbankaccount;

import com.jcondotta.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

/** * Use case interface for looking up bank accounts.
 * This interface defines the contract for looking up a bank account by its ID.
 */
public interface BankAccountLookupUseCase {

    /** * Looks up a bank account by its ID.
     *
     * @param bankAccountId the ID of the bank account to look up
     * @return a response containing the details of the looked-up bank account
     */
    BankAccountLookupResult lookup(BankAccountId bankAccountId);
}