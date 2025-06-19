package com.jcondotta.application.ports.input.service;

import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;

/**
 * Service interface for creating a bank account.
 * This service handles the creation of a bank account based on the provided request.
 */
public interface CreateBankAccountService {

    /**
     * Creates a bank account based on the provided request.
     *
     * @param request the request containing the details for creating the bank account
     * @return a response containing the details of the created bank account
     */
    CreateBankAccountResponse createBankAccount(CreateBankAccountRequest request);
}
