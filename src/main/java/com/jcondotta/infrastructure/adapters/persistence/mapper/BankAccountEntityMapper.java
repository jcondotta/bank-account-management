package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;

import java.util.List;

public interface BankAccountEntityMapper {

    BankingEntity toEntity(BankAccount bankAccount);

    List<BankingEntity> toEntities(BankAccount bankAccount);

    BankAccount toDomain(BankingEntity entity, List<BankingEntity> holderEntities);
}