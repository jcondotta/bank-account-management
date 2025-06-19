package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.value_object.BankAccountId;

/**
 * Repository interface for querying account holders.
 * Extends the BankingEntitiesQueryRepository with BankAccountId as the identifier type.
 */
public interface AccountHoldersQueryRepository extends BankingEntitiesQueryRepository<BankAccountId> {

}
