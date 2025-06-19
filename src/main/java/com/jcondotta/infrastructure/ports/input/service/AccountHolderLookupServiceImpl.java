package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.application.dto.lookup.AccountHolderLookupResponse;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.application.ports.input.service.AccountHolderLookupService;
import com.jcondotta.application.ports.output.repository.AccountHolderLookupRepository;
import com.jcondotta.domain.exception.AccountHolderNotFoundException;
import com.jcondotta.infrastructure.ports.output.repository.AccountHolderLookupRequest;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderLookupServiceImpl implements AccountHolderLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHolderLookupServiceImpl.class);

    private final AccountHolderLookupRepository accountHolderLookupRepository;
    private final BankingEntityMapper accountHolderMapper;
    private final Validator validator;

    public AccountHolderLookupServiceImpl(AccountHolderLookupRepository accountHolderLookupRepository,
                                          BankingEntityMapper accountHolderMapper,
                                          Validator validator) {
        this.accountHolderLookupRepository = accountHolderLookupRepository;
        this.accountHolderMapper = accountHolderMapper;
        this.validator = validator;
    }

    @Override
    public AccountHolderLookupResponse lookup(AccountHolderLookupRequest accountHolderLookupRequest) {
        accountHolderLookupRequest.validateWith(validator);

        LOGGER.info("Performing lookup for account holder: {}", accountHolderLookupRequest);

        return accountHolderLookupRepository.lookup(accountHolderLookupRequest)
            .map(accountHolderMapper::toAccountHolderLookupResponse)
            .orElseThrow(() -> new AccountHolderNotFoundException(
                accountHolderLookupRequest.bankAccountId().toString(),
                accountHolderLookupRequest.accountHolderId().toString()));
    }
}
