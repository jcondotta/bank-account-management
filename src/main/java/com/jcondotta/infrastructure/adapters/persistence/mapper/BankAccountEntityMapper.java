package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.enums.EntityType;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface BankAccountEntityMapper {

    Logger LOGGER = LoggerFactory.getLogger(BankAccountEntityMapper.class);

    default BankingEntity toBankAccountEntity(BankAccount bankAccount) {
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

    default BankAccount toBankAccount(BankingEntity bankAccountEntity,
                                      List<BankingEntity> accountHolderEntities,
                                      @Context AccountHolderEntityMapper accountHolderEntityMapper,
                                      @Context Clock clock) {
        return new BankAccount(
            BankAccountId.of(bankAccountEntity.getBankAccountId()),
            AccountTypeValue.of(bankAccountEntity.getAccountType()),
            CurrencyValue.of(bankAccountEntity.getCurrency()),
            Iban.of(bankAccountEntity.getIban()),
            AccountStatusValue.of(bankAccountEntity.getStatus()),
            CreatedAt.of(bankAccountEntity.getCreatedAt(), clock),
            accountHolderEntities
                .stream()
                .map(accountHolderEntity -> accountHolderEntityMapper.toDomain(accountHolderEntity, clock))
                .toList());
    }

    default List<BankingEntity> toBankingEntities(BankAccount bankAccount, @Context AccountHolderEntityMapper accountHolderEntityMapper) {
        List<BankingEntity> accountHolders = bankAccount.accountHolders().stream()
            .map(holder -> accountHolderEntityMapper.toEntity(bankAccount.bankAccountId(), holder))
            .toList();

        BankingEntity bankAccountEntity = toBankAccountEntity(bankAccount);

        return Stream.concat(Stream.of(bankAccountEntity), accountHolders.stream()).toList();
    }
}