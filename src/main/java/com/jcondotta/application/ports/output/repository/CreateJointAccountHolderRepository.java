package com.jcondotta.application.ports.output.repository;

import com.jcondotta.domain.model.BankingEntity;

public interface CreateJointAccountHolderRepository {

    /**
     * Creates a joint account holder in the repository.
     *
     * @param jointAccountHolder the banking entity representing the joint account holder to be created
     */
    void createJointAccountHolder(BankingEntity jointAccountHolder);
}