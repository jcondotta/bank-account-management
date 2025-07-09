package com.jcondotta.application.usecase.addjointaccountholder.model;

import com.jcondotta.application.usecase.shared.model.AccountHolderDetails;

/**
 * Result of adding a joint account holder.
 * Contains the details of the newly added account holder.
 *
 * @param accountHolder the details of the added joint account holder
 */
public record AddJointAccountHolderResult(AccountHolderDetails accountHolder) {
}
