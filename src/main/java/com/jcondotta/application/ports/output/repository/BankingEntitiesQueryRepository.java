package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.model.BankingEntity;

import java.util.List;

/**
 * Repository interface for querying banking entities.
 * This interface defines a method to query banking entities based on a request of type I.
 *
 * @param <I> the type of the request used to query banking entities
 */
interface BankingEntitiesQueryRepository<I> {

    /**
     * Queries banking entities based on the provided request.
     *
     * @param request the request containing the criteria for querying banking entities
     * @return a list of banking entities that match the criteria
     */
    List<BankingEntity> query(I request);
}
