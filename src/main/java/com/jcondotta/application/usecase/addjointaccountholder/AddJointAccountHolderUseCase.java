package com.jcondotta.application.usecase.addjointaccountholder;

import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.application.usecase.addjointaccountholder.model.AddJointAccountHolderResult;

/** * Use case interface for adding a joint account holder to a bank account.
 * This interface defines the contract for the use case that handles the addition of a joint account holder.
 */
public interface AddJointAccountHolderUseCase {

    /**
     * Adds a joint account holder to a bank account based on the provided command.
     *
     * @param command the command containing the details for adding the joint account holder
     * @return a result indicating the success or failure of the operation
     */
    AddJointAccountHolderResult execute(AddJointAccountHolderCommand command);
}