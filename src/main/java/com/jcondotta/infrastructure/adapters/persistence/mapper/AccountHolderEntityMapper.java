package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderTypeValue;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.shared.enums.EntityType;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.infrastructure.adapters.persistence.entity.AccountHolderKey;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.time.Clock;

@Mapper(componentModel = "spring")
public interface AccountHolderEntityMapper {

    default BankingEntity toEntity(BankAccountId bankAccountId, AccountHolder accountHolder){
        return BankingEntity.builder()
            .partitionKey(AccountHolderKey.partitionKey(bankAccountId))
            .sortKey(AccountHolderKey.sortKey(accountHolder.accountHolderId()))
            .entityType(EntityType.ACCOUNT_HOLDER)
            .bankAccountId(bankAccountId.value())
            .accountHolderId(accountHolder.accountHolderId().value())
            .accountHolderName(accountHolder.accountHolderName().value())
            .passportNumber(accountHolder.passportNumber().value())
            .dateOfBirth(accountHolder.dateOfBirth().value())
            .accountHolderType(accountHolder.accountHolderType().value())
            .createdAt(accountHolder.createdAt().value())
            .build();
    }

    default AccountHolder toDomain(BankingEntity accountHolderEntity){
        return new AccountHolder(
            AccountHolderId.of(accountHolderEntity.getAccountHolderId()),
            AccountHolderName.of(accountHolderEntity.getAccountHolderName()),
            PassportNumber.of(accountHolderEntity.getPassportNumber()),
            DateOfBirth.of(accountHolderEntity.getDateOfBirth()),
            AccountHolderTypeValue.of(accountHolderEntity.getAccountHolderType()),
            CreatedAt.of(accountHolderEntity.getCreatedAt())
        );
    }
}