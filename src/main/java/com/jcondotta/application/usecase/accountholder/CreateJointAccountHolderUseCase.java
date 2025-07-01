package com.jcondotta.application.usecase.accountholder;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderRequest;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;

/**
 * Use case interface for creating a joint account holder.
 * This interface defines a method to create a joint account holder for a given bank account.
 */
public interface CreateJointAccountHolderUseCase {

    /**
     * Creates a joint account holder for the specified bank account.
     *
     * @param bankAccountId the ID of the bank account
     * @param request the request containing details for creating the joint account holder
     * @return the created joint account holder
     */
    BankAccount execute(BankAccountId bankAccountId, CreateJointAccountHolderRequest request);
}