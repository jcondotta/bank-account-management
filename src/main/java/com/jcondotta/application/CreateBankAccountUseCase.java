package com.jcondotta.application;

import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;

/**
 * Use case interface for creating a bank account.
 * This interface defines the contract for creating a bank account based on the provided request.
 */
public interface CreateBankAccountUseCase {

    /**
     * Creates a bank account based on the provided request.
     *
     * @param request the request containing the details for creating the bank account
     * @return a response containing the details of the created bank account
     */
    CreateBankAccountResponse createBankAccount(CreateBankAccountRequest request);
}
