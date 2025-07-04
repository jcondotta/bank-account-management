package com.jcondotta.application.usecase.removejointaccountholder;

import com.jcondotta.application.usecase.removejointaccountholder.model.RemoveJointAccountHolderCommand;

/** * Use case interface for removing a joint account holder from a bank account.
 * This interface defines the contract for the use case that handles the removal
 * of a joint account holder based on the provided command.
 */
public interface RemoveJointAccountHolderUseCase {

    /** Removes a joint account holder from a bank account based on the provided command.
     *
     * @param command the command containing the details for removing the joint account holder
     */
    void execute(RemoveJointAccountHolderCommand command);
}