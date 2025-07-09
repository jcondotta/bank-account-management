package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.application.usecase.shared.helper.TestCreateAccountHolderData;
import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankAccountEntityKey;
import com.jcondotta.infrastructure.adapters.persistence.enums.EntityType;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountEntityMapperImplTest {

    private static final String ACCOUNT_HOLDER_NAME = TestCreateAccountHolderData.JEFFERSON.getAccountHolderName().value();

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    private final AccountHolderEntityMapper accountHolderMapper = new AccountHolderEntityMapperImpl();
    private final BankAccountEntityMapperImpl mapper = new BankAccountEntityMapperImpl(accountHolderMapper);

    @Test
    void shouldMapToEntity_whenBankAccountIsValid() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME, FIXED_CLOCK);

        assertThat(mapper.toEntity(bankAccount))
            .satisfies(bankAccountEntity -> {
                assertThat(bankAccountEntity.getPartitionKey()).isEqualTo(BankAccountEntityKey.partitionKey(bankAccount.bankAccountId()));
                assertThat(bankAccountEntity.getSortKey()).isEqualTo(BankAccountEntityKey.sortKey(bankAccount.bankAccountId()));
                assertThat(bankAccountEntity.getEntityType()).isEqualTo(EntityType.BANK_ACCOUNT);
                assertThat(bankAccountEntity.getBankAccountId()).isEqualTo(bankAccount.bankAccountId().value());
                assertThat(bankAccountEntity.getAccountType()).isEqualTo(bankAccount.accountType());
                assertThat(bankAccountEntity.getCurrency()).isEqualTo(bankAccount.currency());
                assertThat(bankAccountEntity.getIban()).isEqualTo(bankAccount.iban().value());
                assertThat(bankAccountEntity.getStatus()).isEqualTo(bankAccount.status());
                assertThat(bankAccountEntity.getCreatedAt()).isEqualTo(bankAccount.createdAt().value());
                assertThat(bankAccountEntity.getCreatedAtZoneId()).isEqualTo(bankAccount.createdAt().zoneId());
            });
    }

    @Test
    void shouldMapToMultipleEntities_whenBankAccountHasPrimaryAndJointHolders() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME, FIXED_CLOCK);
        var primaryAccountHolder = bankAccount.getPrimaryAccountHolder();

        var jointAccountHolder1 = TestAccountHolderFactory.createJointWithName("Joint Account Holder #1");
        var jointAccountHolder2 = TestAccountHolderFactory.createJointWithName("Joint Account Holder #2");

        Stream.of(jointAccountHolder1, jointAccountHolder2).forEach(bankAccount::addJointAccountHolder);

        assertThat(mapper.toEntities(bankAccount))
            .hasSize(4)
            .anySatisfy(bankingEntity -> {
                assertThat(bankingEntity.getEntityType()).isEqualTo(EntityType.BANK_ACCOUNT);
                assertThat(bankingEntity.getBankAccountId()).isEqualTo(bankAccount.bankAccountId().value());
                assertThat(bankingEntity.getAccountType()).isEqualTo(bankAccount.accountType());
                assertThat(bankingEntity.getCurrency()).isEqualTo(bankAccount.currency());
                assertThat(bankingEntity.getIban()).isEqualTo(bankAccount.iban().value());
                assertThat(bankingEntity.getStatus()).isEqualTo(bankAccount.status());
                assertThat(bankingEntity.getCreatedAt()).isEqualTo(bankAccount.createdAt().value());
                assertThat(bankingEntity.getCreatedAtZoneId()).isEqualTo(bankAccount.createdAt().zoneId());
            })
            .anySatisfy(bankingEntity -> {
                assertThat(bankingEntity.getEntityType()).isEqualTo(EntityType.ACCOUNT_HOLDER);
                assertThat(bankingEntity.getBankAccountId()).isEqualTo(bankAccount.bankAccountId().value());
                assertThat(bankingEntity.getAccountHolderId()).isEqualTo(primaryAccountHolder.accountHolderId().value());
                assertThat(bankingEntity.getAccountType()).isNull();
                assertThat(bankingEntity.getCurrency()).isNull();
                assertThat(bankingEntity.getIban()).isNull();
                assertThat(bankingEntity.getStatus()).isNull();
                assertThat(bankingEntity.getCreatedAt()).isEqualTo(primaryAccountHolder.createdAt().value());
                assertThat(bankingEntity.getCreatedAtZoneId()).isEqualTo(primaryAccountHolder.createdAt().zoneId());
            })
            .anySatisfy(bankingEntity -> {
                assertThat(bankingEntity.getEntityType()).isEqualTo(EntityType.ACCOUNT_HOLDER);
                assertThat(bankingEntity.getBankAccountId()).isEqualTo(bankAccount.bankAccountId().value());
                assertThat(bankingEntity.getAccountHolderId()).isEqualTo(jointAccountHolder1.accountHolderId().value());
                assertThat(bankingEntity.getAccountType()).isNull();
                assertThat(bankingEntity.getCurrency()).isNull();
                assertThat(bankingEntity.getIban()).isNull();
                assertThat(bankingEntity.getStatus()).isNull();
                assertThat(bankingEntity.getCreatedAt()).isEqualTo(jointAccountHolder1.createdAt().value());
                assertThat(bankingEntity.getCreatedAtZoneId()).isEqualTo(jointAccountHolder1.createdAt().zoneId());
            })
            .anySatisfy(bankingEntity -> {
                assertThat(bankingEntity.getEntityType()).isEqualTo(EntityType.ACCOUNT_HOLDER);
                assertThat(bankingEntity.getBankAccountId()).isEqualTo(bankAccount.bankAccountId().value());
                assertThat(bankingEntity.getAccountHolderId()).isEqualTo(jointAccountHolder2.accountHolderId().value());
                assertThat(bankingEntity.getAccountType()).isNull();
                assertThat(bankingEntity.getCurrency()).isNull();
                assertThat(bankingEntity.getIban()).isNull();
                assertThat(bankingEntity.getStatus()).isNull();
                assertThat(bankingEntity.getCreatedAt()).isEqualTo(jointAccountHolder2.createdAt().value());
                assertThat(bankingEntity.getCreatedAtZoneId()).isEqualTo(jointAccountHolder2.createdAt().zoneId());
            });
    }

    @Test
    void shouldMapToDomain_whenEntityAndHoldersAreValid() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME, FIXED_CLOCK);

        var bankAccountEntity = mapper.toEntity(bankAccount);
        var holderEntities = bankAccount.accountHolders().stream()
            .map(ah -> accountHolderMapper.toEntity(bankAccount.bankAccountId(), ah))
            .toList();

        assertThat(mapper.toDomain(bankAccountEntity, holderEntities))
            .satisfies(b -> {
                assertThat(b.bankAccountId()).isEqualTo(bankAccount.bankAccountId());
                assertThat(b.accountType()).isEqualTo(bankAccount.accountType());
                assertThat(b.currency()).isEqualTo(bankAccount.currency());
                assertThat(b.iban()).isEqualTo(bankAccount.iban());
                assertThat(b.status()).isEqualTo(bankAccount.status());
                assertThat(b.createdAt().value()).isEqualTo(bankAccount.createdAt().value());
                assertThat(b.createdAt().zoneId()).isEqualTo(bankAccount.createdAt().zoneId());

                assertThat(b.accountHolders()).hasSize(1);
                var original = bankAccount.getPrimaryAccountHolder();
                var mapped = b.getPrimaryAccountHolder();

                assertThat(mapped.accountHolderId()).isEqualTo(original.accountHolderId());
                assertThat(mapped.accountHolderName()).isEqualTo(original.accountHolderName());
                assertThat(mapped.passportNumber()).isEqualTo(original.passportNumber());
                assertThat(mapped.dateOfBirth()).isEqualTo(original.dateOfBirth());
                assertThat(mapped.accountHolderType()).isEqualTo(AccountHolderType.PRIMARY);
                assertThat(mapped.createdAt().value()).isEqualTo(original.createdAt().value());
                assertThat(mapped.createdAt().zoneId()).isEqualTo(original.createdAt().zoneId());
            });
    }
}
