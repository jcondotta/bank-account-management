package com.jcondotta.application.ports.input.service;

import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import com.jcondotta.domain.value_object.BankAccountId;

/**
 * Service interface for querying account holders associated with a bank account.
 * Extends the BankingEntityLookupService with BankAccountId as the identifier type
 * and AccountHoldersQueryResponse as the response type.
 */
public interface AccountHoldersQueryService extends BankingEntityLookupService<BankAccountId, AccountHoldersQueryResponse> {
}
