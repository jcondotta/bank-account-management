package com.jcondotta.application.ports.input.service;

import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.domain.value_object.BankAccountId;

/**
 * Service interface for querying bank accounts.
 * Extends the BankingEntityLookupService with BankAccountId as the identifier type
 * and BankAccountLookupResponse as the response type.
 */
public interface BankAccountLookupService extends BankingEntityLookupService<BankAccountId, BankAccountLookupResponse>{
}
