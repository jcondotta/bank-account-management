package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.application.ports.input.service.AccountHoldersQueryService;
import com.jcondotta.application.ports.output.repository.AccountHoldersQueryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountHoldersQueryServiceImpl implements AccountHoldersQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHoldersQueryServiceImpl.class);

    private final AccountHoldersQueryRepository accountHoldersQueryRepository;
    private final BankingEntityMapper accountHolderMapper;

    @Override
    public AccountHoldersQueryResponse lookup(BankAccountId bankAccountId) {
        var accountHolderEntities = accountHoldersQueryRepository.query(bankAccountId);
        return accountHolderMapper.toAccountHoldersQueryResponse(accountHolderEntities);
    }
}