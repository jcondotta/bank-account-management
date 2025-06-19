package com.jcondotta.application.ports.input.service;

import com.jcondotta.application.dto.create.CreateJointAccountHolderRequest;
import com.jcondotta.application.dto.create.CreateJointAccountHolderResponse;
import com.jcondotta.domain.value_object.BankAccountId;

import java.util.UUID;

/**
 * Service interface for creating a joint account holder.
 * This interface defines a method to create a joint account holder for a given bank account.
 */
public interface CreateJointAccountHolderService {

    /**
     * Creates a joint account holder for the specified bank account.
     *
     * @param bankAccountId the ID of the bank account
     * @param request the request containing details for creating the joint account holder
     * @return a response containing details of the created joint account holder
     */
    CreateJointAccountHolderResponse createJointAccountHolder(BankAccountId bankAccountId, CreateJointAccountHolderRequest request);
}
