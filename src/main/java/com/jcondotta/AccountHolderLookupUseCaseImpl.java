package com.jcondotta;

import com.jcondotta.application.AccountHolderLookupUseCase;
import com.jcondotta.application.dto.lookup.AccountHolderLookupResponse;
import com.jcondotta.application.ports.input.service.AccountHolderLookupService;
import com.jcondotta.infrastructure.ports.output.repository.AccountHolderLookupRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountHolderLookupUseCaseImpl implements AccountHolderLookupUseCase {

    private final AccountHolderLookupService accountHolderLookupService;

    public AccountHolderLookupUseCaseImpl(AccountHolderLookupService accountHolderLookupService) {
        this.accountHolderLookupService = accountHolderLookupService;
    }

    @Override
    public AccountHolderLookupResponse lookup(AccountHolderLookupRequest accountHolderLookupRequest) {
        return accountHolderLookupService.lookup(accountHolderLookupRequest);
    }
}