package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.model.BankingEntity;

import java.util.Optional;

/**
 * Repository interface for looking up banking entities.
 * This interface defines a method to look up a banking entity based on a request of type I.
 *
 * @param <I> the type of the request used to look up the banking entity
 */
interface BankingEntitiesLookupRepository<I> {

    /**
     * Looks up a banking entity based on the provided request.
     *
     * @param request the request containing the criteria for looking up the banking entity
     * @return the banking entity that matches the criteria, or null if not found
     */
    Optional<BankingEntity> lookup(I request);
}
