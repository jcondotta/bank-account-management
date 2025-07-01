package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.bankaccount.model.BankAccount;

/** * Repository interface for updating bank accounts.
 * This interface defines a method to update a BankAccount and its associated account holders.
 */
public interface UpdateBankAccountRepository {

    /**
     * Updates a BankAccount and its associated account holders in the repository.
     *
     * @param bankAccount the BankAccount to be updated
     */
    void update(BankAccount bankAccount);
}