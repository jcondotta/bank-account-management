package com.jcondotta.application.ports.output.repository;

import com.jcondotta.infrastructure.ports.output.repository.AccountHolderLookupRequest;

/**
 * Repository interface for looking up account holders.
 * Extends the BankingEntitiesLookupRepository with AccountHolderLookup as the entity type.
 */
public interface AccountHolderLookupRepository extends BankingEntitiesLookupRepository<AccountHolderLookupRequest> {

}
