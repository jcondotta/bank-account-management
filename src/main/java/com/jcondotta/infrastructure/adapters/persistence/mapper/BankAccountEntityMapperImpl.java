package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountEntityKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.enums.EntityType;
import lombok.AllArgsConstructor;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class BankAccountEntityMapperImpl implements BankAccountEntityMapper {

    private final AccountHolderEntityMapper accountHolderEntityMapper;

    public BankingEntity toEntity(BankAccount bankAccount) {
        return BankingEntity.builder()
            .partitionKey(BankAccountEntityKey.partitionKey(bankAccount.bankAccountId()))
            .sortKey(BankAccountEntityKey.sortKey(bankAccount.bankAccountId()))
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

    @Override
    public List<BankingEntity> toEntities(BankAccount bankAccount) {
        var bankAccountEntity = toEntity(bankAccount);
        var accountHolders = bankAccount.accountHolders().stream()
            .map(accountHolder -> accountHolderEntityMapper.toEntity(bankAccount.bankAccountId(), accountHolder))
            .toList();

        return Stream.concat(Stream.of(bankAccountEntity), accountHolders.stream()).toList();
    }

    public BankAccount toDomain(BankingEntity entity, List<BankingEntity> holderEntities) {
        return new BankAccount(
            BankAccountId.of(entity.getBankAccountId()),
            entity.getAccountType(),
            entity.getCurrency(),
            Iban.of(entity.getIban()),
            entity.getStatus(),
            CreatedAt.of(entity.getCreatedAt(), entity.getCreatedAtZoneId()),
            holderEntities.stream()
                .map(accountHolderEntityMapper::toDomain)
                .toList()
        );
    }
}