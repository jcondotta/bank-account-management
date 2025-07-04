package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

public interface AddJointAccountHolderRepository {

    /**
     * Saves a joint account holder associated with a bank account.
     *
     * @param bankAccountId the ID of the bank account to which the joint account holder is associated
     * @param accountHolder the joint account holder to be saved
     */
    void save(BankAccountId bankAccountId, AccountHolder accountHolder);
}