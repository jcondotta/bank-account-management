package com.jcondotta.application.usecase.createbankaccount;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;

/**
 * Use case interface for creating a bank account.
 * This interface defines the contract for creating a bank account based on the provided request.
 */
public interface CreateBankAccountUseCase {

    /** Creates a bank account based on the provided command.
     *
     * @param command the command containing the details for creating the bank account
     * @return a result containing the ID of the created bank account
     */
    CreateBankAccountResult execute(CreateBankAccountCommand command);
}
