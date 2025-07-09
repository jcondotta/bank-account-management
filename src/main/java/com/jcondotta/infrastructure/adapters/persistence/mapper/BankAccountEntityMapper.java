package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.enums.EntityType;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountEntityMapper {

    Logger LOGGER = LoggerFactory.getLogger(BankAccountEntityMapper.class);

    default BankingEntity toEntity(BankAccount bankAccount) {
        return BankingEntity.builder()
            .partitionKey(BankAccountKey.partitionKey(bankAccount.bankAccountId()))
            .sortKey(BankAccountKey.sortKey(bankAccount.bankAccountId()))
            .entityType(EntityType.BANK_ACCOUNT)
            .bankAccountId(bankAccount.bankAccountId().value())
            .accountType(bankAccount.accountType())
            .currency(bankAccount.currency())
            .iban(bankAccount.iban().value())
            .status(bankAccount.status())
            .createdAt(bankAccount.createdAt().value())
            .createdAtZoneId(bankAccount.createdAt().zoneId())
            .build();
    }

    default BankAccount toDomain(BankingEntity entity,
                                 List<BankingEntity> holderEntities,
                                 @Context AccountHolderEntityMapper holderMapper) {
        return new BankAccount(
            BankAccountId.of(entity.getBankAccountId()),
            entity.getAccountType(),
            entity.getCurrency(),
            Iban.of(entity.getIban()),
            entity.getStatus(),
            CreatedAt.of(entity.getCreatedAt(), entity.getCreatedAtZoneId()),
            holderEntities.stream()
                .map(holderMapper::toDomain)
                .toList()
        );
    }
}