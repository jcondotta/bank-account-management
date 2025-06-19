package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.application.ports.input.service.BankAccountLookupService;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.application.ports.output.repository.BankAccountQueryRepository;
import com.jcondotta.domain.exception.BankAccountNotFoundException;
import com.jcondotta.domain.model.BankingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BankAccountLookupServiceImpl implements BankAccountLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountLookupServiceImpl.class);

    private final BankAccountQueryRepository bankingEntitiesLookupRepository;
    private final BankingEntityMapper bankingEntityMapper;

    public BankAccountLookupServiceImpl(BankAccountQueryRepository bankingEntitiesLookupRepository, BankingEntityMapper bankingEntityMapper) {
        this.bankingEntitiesLookupRepository = bankingEntitiesLookupRepository;
        this.bankingEntityMapper = bankingEntityMapper;
    }

    @Override
    public BankAccountLookupResponse lookup(BankAccountId bankAccountId) {
        var bankingEntities = bankingEntitiesLookupRepository.query(bankAccountId);

        var bankAccount = bankingEntities.stream()
                .filter(BankingEntity::isEntityTypeBankAccount)
                .findFirst()
                .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId.value()));

        return bankingEntityMapper.toBankAccountLookupByIdResponse(bankAccount, bankingEntities.stream()
            .filter(BankingEntity::isEntityTypeAccountHolder)
            .toList());
    }
}
