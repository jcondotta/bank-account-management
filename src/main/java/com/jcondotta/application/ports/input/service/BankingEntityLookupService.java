package com.jcondotta.application.ports.input.service;

/**
 * Generic service interface for looking up banking entities.
 *
 * @param <I> the type of the request object
 * @param <O> the type of the response object
 */
interface BankingEntityLookupService<I, O> {

    /**
     * Looks up a banking entity based on the provided request.
     *
     * @param request the request containing the criteria for looking up the banking entity
     * @return the response containing the details of the banking entity, or null if not found
     */
    O lookup(I request);
}
