package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.bankaccount.model.BankAccount;

/**
 * Repository interface for querying banking entities.
 * This interface defines a method to query banking entities based on a request of type BankingEntity.
 */
public interface CreateBankAccountRepository {

    /**
     * Saves a BankAccount and its associated account holders to the repository.
     *
     * @param bankAccount the BankAccount to be saved
     */
    void save(BankAccount bankAccount);
}