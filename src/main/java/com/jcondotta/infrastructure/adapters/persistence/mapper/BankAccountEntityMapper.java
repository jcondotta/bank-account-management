package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.enums.EntityType;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
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
            .accountType(bankAccount.accountType().value())
            .currency(bankAccount.currency().value())
            .iban(bankAccount.iban().value())
            .status(bankAccount.status().value())
            .createdAt(bankAccount.createdAt().value())
            .build();
    }

    default BankAccount toDomain(BankingEntity entity,
                                 List<BankingEntity> holderEntities,
                                 @Context AccountHolderEntityMapper holderMapper,
                                 @Context Clock clock) {
        return new BankAccount(
            BankAccountId.of(entity.getBankAccountId()),
            AccountTypeValue.of(entity.getAccountType()),
            CurrencyValue.of(entity.getCurrency()),
            Iban.of(entity.getIban()),
            AccountStatusValue.of(entity.getStatus()),
            CreatedAt.of(entity.getCreatedAt()),
            holderEntities.stream()
                .map(holderMapper::toDomain)
                .toList()
        );
    }
}