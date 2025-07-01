package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

/**
 * Repository interface for querying bank accounts.
 * Extends the BankingEntitiesQueryRepository with BankAccountId as the identifier type.
 */
public interface BankAccountLookupRepository extends BankingEntitiesLookupRepository<BankAccountId, BankAccount> {
}
