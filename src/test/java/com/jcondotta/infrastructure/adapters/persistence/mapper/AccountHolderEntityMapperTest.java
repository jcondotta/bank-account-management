package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.application.usecase.shared.helper.TestCreateAccountHolderData;
import com.jcondotta.infrastructure.adapters.persistence.entity.AccountHolderEntityKey;
import com.jcondotta.infrastructure.adapters.persistence.enums.EntityType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderEntityMapperTest {

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    private final AccountHolderEntityMapper mapper = new AccountHolderEntityMapperImpl();

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldMapToEntity_whenAccountHolderIsValid(AccountHolderType accountHolderType) {
        var bankAccountId = BankAccountId.newId();

        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();
        var accountHolder = AccountHolder.create(accountHolderData, accountHolderType, FIXED_CLOCK);

        assertThat(mapper.toEntity(bankAccountId, accountHolder))
            .satisfies(accountHolderEntity -> {
                assertThat(accountHolderEntity.getPartitionKey()).isEqualTo(AccountHolderEntityKey.partitionKey(bankAccountId));
                assertThat(accountHolderEntity.getSortKey()).isEqualTo(AccountHolderEntityKey.sortKey(accountHolder.accountHolderId()));
                assertThat(accountHolderEntity.getEntityType()).isEqualTo(EntityType.ACCOUNT_HOLDER);
                assertThat(accountHolderEntity.getBankAccountId()).isEqualTo(bankAccountId.value());
                assertThat(accountHolderEntity.getAccountHolderId()).isEqualTo(accountHolder.accountHolderId().value());
                assertThat(accountHolderEntity.getAccountHolderName()).isEqualTo(accountHolder.accountHolderName().value());
                assertThat(accountHolderEntity.getPassportNumber()).isEqualTo(accountHolder.passportNumber().value());
                assertThat(accountHolderEntity.getDateOfBirth()).isEqualTo(accountHolder.dateOfBirth().value());
                assertThat(accountHolderEntity.getAccountHolderType()).isEqualTo(accountHolderType);
                assertThat(accountHolderEntity.getCreatedAt()).isEqualTo(accountHolder.createdAt().value());
                assertThat(accountHolderEntity.getCreatedAtZoneId()).isEqualTo(accountHolder.createdAt().zoneId());
            });
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldMapToDomain_whenEntityIsValid(AccountHolderType accountHolderType) {
        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();
        var accountHolder = AccountHolder.create(accountHolderData, accountHolderType, FIXED_CLOCK);

        var accountHolderEntity = mapper.toEntity(BankAccountId.newId(), accountHolder);

        assertThat(mapper.toDomain(accountHolderEntity))
            .satisfies(holder -> {
                assertThat(holder.accountHolderId().value()).isEqualTo(accountHolderEntity.getAccountHolderId());
                assertThat(holder.accountHolderName().value()).isEqualTo(accountHolderEntity.getAccountHolderName());
                assertThat(holder.passportNumber().value()).isEqualTo(accountHolderEntity.getPassportNumber());
                assertThat(holder.dateOfBirth().value()).isEqualTo(accountHolderEntity.getDateOfBirth());
                assertThat(holder.accountHolderType()).isEqualTo(accountHolderEntity.getAccountHolderType());
                assertThat(holder.createdAt().value()).isEqualTo(accountHolderEntity.getCreatedAt());
                assertThat(holder.createdAt().zoneId()).isEqualTo(accountHolderEntity.getCreatedAtZoneId());
            });
    }
}
