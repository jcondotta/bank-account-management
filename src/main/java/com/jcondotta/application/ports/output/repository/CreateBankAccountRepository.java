package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.model.BankingEntity;

import java.util.List;

/**
 * Repository interface for querying banking entities.
 * This interface defines a method to query banking entities based on a request of type BankingEntity.
 */
public interface CreateBankAccountRepository {

    /**
     * Creates a bank account with a single account holder.
     *
     * @param bankAccount the bank account to be created
     * @param accountHolder the account holder associated with the bank account
     */
    void createBankAccount(BankingEntity bankAccount, BankingEntity accountHolder);

    /**
     * Creates a bank account with multiple account holders.
     *
     * @param bankAccount the bank account to be created
     * @param accountHolders the list of account holders associated with the bank account
     */
    void createBankAccount(BankingEntity bankAccount, List<BankingEntity> accountHolders);
}