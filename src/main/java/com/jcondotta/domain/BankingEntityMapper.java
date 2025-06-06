package com.jcondotta.domain;

import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import net.datafaker.Faker;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BankingEntityMapper {

    BankingEntityMapper INSTANCE = Mappers.getMapper(BankingEntityMapper.class);
    Logger LOGGER = LoggerFactory.getLogger(BankingEntityMapper.class);

    default BankingEntity toBankAccount(UUID bankAccountId, String iban, @Context Clock clock) {
        LOGGER.info("Mapping a bank account object");

        return BankingEntity.builder()
                .partitionKey(BankingEntity.ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId))
                .sortKey(BankingEntity.BANK_ACCOUNT_SK_TEMPLATE.formatted(bankAccountId))
                .entityType(EntityType.BANK_ACCOUNT)
                .bankAccountId(bankAccountId)
                .iban(iban)
                .createdAt(LocalDateTime.now(clock))
                .build();
    }

    default BankingEntity toBankAccount(@Context Clock clock) {
        var bankAccountId = UUID.randomUUID();
        var iban = new Faker().finance().iban();

        return toBankAccount(bankAccountId, iban,  clock);
    }

    default BankingEntity toAccountHolder(
            UUID bankAccountId,
            CreateAccountHolderRequest request,
            AccountHolderType type,
            @Context Clock clock) {

        LOGGER.info("Mapping an account holder object for bank account ID: {}", bankAccountId);

        var accountHolderId = UUID.randomUUID();
        return BankingEntity.builder()
                .partitionKey(BankingEntity.ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId))
                .sortKey(BankingEntity.ACCOUNT_HOLDER_SK_TEMPLATE.formatted(accountHolderId))
                .entityType(EntityType.ACCOUNT_HOLDER)
                .bankAccountId(bankAccountId)
                .accountHolderId(accountHolderId)
                .accountHolderName(request.accountHolderName())
                .passportNumber(request.passportNumber())
                .dateOfBirth(request.dateOfBirth())
                .accountHolderType(type)
                .createdAt(LocalDateTime.now(clock))
                .build();
    }

    default BankingEntity toPrimaryAccountHolder(UUID bankAccountId, CreateAccountHolderRequest request, @Context Clock clock) {
        return toAccountHolder(bankAccountId, request, AccountHolderType.PRIMARY, clock);
    }

    default BankingEntity toJointAccountHolder(UUID bankAccountId, CreateAccountHolderRequest request, @Context Clock clock) {
        return toAccountHolder(bankAccountId, request, AccountHolderType.JOINT, clock);
    }

    // 2. ========== DTO Mappings ==========

    default BankAccountDTO toDto(BankingEntity bankAccount, List<BankingEntity> accountHolders) {
        return mapToBankAccountDTO(
                bankAccount,
                mapAccountHoldersToDTOs(accountHolders)
        );
    }

    default BankAccountDTO toDto(BankingEntity bankAccount, BankingEntity primaryAccountHolder) {
        return toDto(bankAccount, List.of(primaryAccountHolder));
    }

    @Named("toAccountHolderDto")
    AccountHolderDTO toAccountHolderDto(BankingEntity accountHolder);

    private BankingEntity buildAccountHolder(UUID bankAccountId, UUID accountHolderId,
                                             CreateAccountHolderRequest request, AccountHolderType type,
                                             LocalDateTime createdAt) {
        return BankingEntity.builder()
                .partitionKey(BankingEntity.ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId))
                .sortKey(BankingEntity.ACCOUNT_HOLDER_SK_TEMPLATE.formatted(accountHolderId))
                .entityType(EntityType.ACCOUNT_HOLDER)
                .bankAccountId(bankAccountId)
                .accountHolderId(accountHolderId)
                .accountHolderName(request.accountHolderName())
                .passportNumber(request.passportNumber())
                .dateOfBirth(request.dateOfBirth())
                .accountHolderType(type)
                .createdAt(createdAt)
                .build();
    }

    private List<AccountHolderDTO> mapAccountHoldersToDTOs(List<BankingEntity> entities) {
        return entities.stream()
                .map(this::toAccountHolderDto)
                .toList();
    }

    private BankAccountDTO mapToBankAccountDTO(BankingEntity bankAccount, List<AccountHolderDTO> holders) {
        return BankAccountDTO.builder()
                .bankAccountId(bankAccount.getBankAccountId())
                .iban(bankAccount.getIban())
                .dateOfOpening(bankAccount.getCreatedAt())
                .accountHolders(holders)
                .build();
    }
}

