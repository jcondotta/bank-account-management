package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

import java.util.Optional;

/** * Repository interface for looking up bank accounts.
 * This interface defines a method to look up a bank account based on a BankAccountId.
 */
public interface BankAccountLookupRepository {

    /**
     * Looks up a bank account based on the provided BankAccountId.
     *
     * @param request the BankAccountId used to look up the bank account
     * @return an Optional containing the BankAccount if found, or empty if not found
     */
    Optional<BankAccount> lookup(BankAccountId request);
}
