package com.jcondotta.application.mapper;

import com.jcondotta.application.dto.AccountHolderDetailsRequest;
import com.jcondotta.application.dto.AccountHolderDetailsResponse;
import com.jcondotta.domain.value_object.AccountHolderId;
import com.jcondotta.application.dto.create.CreateJointAccountHolderRequest;
import com.jcondotta.application.dto.create.CreateJointAccountHolderResponse;
import com.jcondotta.application.dto.lookup.AccountHolderLookupResponse;
import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import com.jcondotta.domain.model.AccountHolderType;
import com.jcondotta.domain.model.BankingEntity;
import com.jcondotta.domain.model.EntityType;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AccountHolderId.class)
public interface AccountHolderMapper {

    Logger LOGGER = LoggerFactory.getLogger(AccountHolderMapper.class);

    default BankingEntity toPrimaryAccountHolderEntity(UUID accountHolderId, UUID bankAccountId, AccountHolderDetailsRequest request, @Context Clock clock) {
        return toAccountHolderEntity(accountHolderId, bankAccountId, request, AccountHolderType.PRIMARY, clock);
    }

    default BankingEntity toJointAccountHolderEntity(UUID accountHolderId, UUID bankAccountId, CreateJointAccountHolderRequest request, @Context Clock clock) {
        return toAccountHolderEntity(accountHolderId, bankAccountId, request.accountHolder(), AccountHolderType.JOINT, clock);
    }

    default BankingEntity toAccountHolderEntity(UUID accountHolderId,
                                                UUID bankAccountId,
                                                AccountHolderDetailsRequest request,
                                                AccountHolderType type,
                                                @Context Clock currentClock) {

        LOGGER.debug("Mapping an account holder object for bank account ID: {}", bankAccountId);

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
            .createdAt(LocalDateTime.now(currentClock))
            .build();
    }

    default AccountHolderDetailsResponse toAccountHolderDetailsResponse(BankingEntity bankingEntity){
        LOGGER.info("Mapping BankingEntity to AccountHolderDTO for account holder ID: {}", bankingEntity.getAccountHolderId());

        return AccountHolderDetailsResponse.builder()
            .accountHolderId(bankingEntity.getAccountHolderId())
            .bankAccountId(bankingEntity.getBankAccountId())
            .accountHolderName(bankingEntity.getAccountHolderName())
            .passportNumber(bankingEntity.getPassportNumber())
            .dateOfBirth(bankingEntity.getDateOfBirth())
            .accountHolderType(bankingEntity.getAccountHolderType())
            .createdAt(bankingEntity.getCreatedAt())
            .build();
    }

    @Named("toCreateJointAccountHolderRequest")
    @Mapping(target = "accountHolderId", source = "accountHolderId", qualifiedByName = "toAccountHolderId")
    CreateJointAccountHolderResponse toCreateJointAccountHolderRequest(BankingEntity accountHolder);

    @Named("toAccountHolderId")
    default AccountHolderId toAccountHolderId(UUID accountHolderId) {
        return Optional.ofNullable(accountHolderId)
            .map(AccountHolderId::new)
            .orElse(null);
    }

    default AccountHolderLookupResponse toAccountHolderLookupResponse(BankingEntity accountHolderEntity) {
        LOGGER.info("Mapping BankingEntity to AccountHolderLookupResponse for account holder with ID: {}",
            accountHolderEntity.getAccountHolderId());

        return AccountHolderLookupResponse.builder()
            .accountHolder(toAccountHolderDetailsResponse(accountHolderEntity))
            .build();
    }

    default AccountHoldersQueryResponse toAccountHoldersQueryResponse(List<BankingEntity> accountHolderEntities) {
//        LOGGER.info("Mapping BankingEntity to AccountHolderLookupResponse for account holder with ID: {}",
//            accountHolderEntity.getAccountHolderId());

        return AccountHoldersQueryResponse.builder()
            .accountHolders(accountHolderEntities
                .stream()
                .map(this::toAccountHolderDetailsResponse)
                .toList())
            .build();
    }
}