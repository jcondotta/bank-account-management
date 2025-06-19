package com.jcondotta.application;

import com.jcondotta.application.dto.lookup.AccountHolderLookupResponse;
import com.jcondotta.infrastructure.ports.output.repository.AccountHolderLookupRequest;

/**
 * Use case interface for looking up account holder information.
 * This interface defines the contract for the use case that retrieves
 * account holder details based on a given request.
 */
public interface AccountHolderLookupUseCase {

    /**
     * Looks up account holder information based on the provided request.
     *
     * @param request the request containing details for the lookup
     * @return a response containing the account holder information
     */
    AccountHolderLookupResponse lookup(AccountHolderLookupRequest request);
}