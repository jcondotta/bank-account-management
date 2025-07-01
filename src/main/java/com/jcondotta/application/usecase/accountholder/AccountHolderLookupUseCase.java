package com.jcondotta.application.usecase.accountholder;

import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupRequest;

/** * Use case interface for looking up account holders.
 * This interface defines the contract for looking up an account holder based on a request.
 */
public interface AccountHolderLookupUseCase {

    /** * Looks up an account holder based on the provided request.
     *
     * @param request the request containing details for looking up the account holder
     * @return the account holder if found
     */
    AccountHolder lookup(AccountHolderLookupRequest request);
}