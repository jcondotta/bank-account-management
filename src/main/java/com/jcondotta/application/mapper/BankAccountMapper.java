package com.jcondotta.application.mapper;

import com.jcondotta.application.dto.BankAccountDetailsResponse;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;
import com.jcondotta.domain.model.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = BankAccountId.class)
public interface BankAccountMapper {

    Logger LOGGER = LoggerFactory.getLogger(BankAccountMapper.class);

    default BankingEntity toBankAccountEntity(UUID bankAccountId, AccountType accountType, Currency currency, String iban, BankAccountStatus status, @Context Clock clock) {
        LOGGER.debug("Mapping a bank account object for bank account ID: {}", bankAccountId);

        return BankingEntity.builder()
            .partitionKey(BankingEntity.BANK_ACCOUNT_PK_TEMPLATE.formatted(bankAccountId))
            .sortKey(BankingEntity.BANK_ACCOUNT_SK_TEMPLATE.formatted(bankAccountId))
            .entityType(EntityType.BANK_ACCOUNT)
            .bankAccountId(bankAccountId)
            .accountType(accountType)
            .currency(currency)
            .iban(iban)
            .status(status)
            .createdAt(LocalDateTime.now(clock))
            .build();
    }
}