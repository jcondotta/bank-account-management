package com.jcondotta.application.ports.input.service;

import com.jcondotta.application.dto.lookup.AccountHolderLookupResponse;
import com.jcondotta.infrastructure.ports.output.repository.AccountHolderLookupRequest;

/**
 * Service interface for looking up account holders.
 * Extends the BankingEntityLookupService with AccountHolderLookupRequest as the request type
 * and AccountHolderLookupResponse as the response type.
 */
public interface AccountHolderLookupService
    extends BankingEntityLookupService<AccountHolderLookupRequest, AccountHolderLookupResponse> {
}
