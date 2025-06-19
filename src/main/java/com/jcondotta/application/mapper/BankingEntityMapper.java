package com.jcondotta.application.mapper;

import com.jcondotta.application.dto.BankAccountDetailsResponse;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;
import com.jcondotta.domain.value_object.AccountHolderId;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.domain.model.BankingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Mapper(componentModel = "spring",
    uses = { AccountHolderId.class, BankAccountId.class })
public interface BankingEntityMapper extends BankAccountMapper, AccountHolderMapper {

    BankingEntityMapper INSTANCE = Mappers.getMapper(BankingEntityMapper.class);
    Logger LOGGER = LoggerFactory.getLogger(BankingEntityMapper.class);

    default BankAccountLookupResponse toBankAccountLookupByIdResponse(BankingEntity bankAccount, List<BankingEntity> accountHolders){
        LOGGER.info("Mapping BankingEntity to BankAccountLookupByIdResponse for bank account ID: {}", bankAccount.getBankAccountId());

        return new BankAccountLookupResponse(toBankAccountDetailsResponse(bankAccount, accountHolders));
    }

    default AccountHoldersQueryResponse toAccountHoldersLookupByBankAccountIdResponse(List<BankingEntity> accountHoldersEntity){
        LOGGER.info("Mapping BankingEntity to AccountHoldersLookupByBankAccountIdResponse for bank account ID: {}", accountHoldersEntity.get(0).getBankAccountId());

        return new AccountHoldersQueryResponse(
            accountHoldersEntity.stream()
                .map(this::toAccountHolderDetailsResponse)
                .toList()
        );
    }

    default BankAccountDetailsResponse toBankAccountDetailsResponse(BankingEntity bankingEntity, List<BankingEntity> accountHolders) {
        LOGGER.debug("Mapping BankingEntity to BankAccountDTO for bank account ID: {}", bankingEntity.getBankAccountId());

        return BankAccountDetailsResponse.builder()
            .bankAccountId(bankingEntity.getBankAccountId())
            .accountType(bankingEntity.getAccountType())
            .currency(bankingEntity.getCurrency())
            .iban(bankingEntity.getIban())
            .status(bankingEntity.getStatus())
            .dateOfOpening(bankingEntity.getCreatedAt())
            .accountHolders(accountHolders.stream()
                .map(this::toAccountHolderDetailsResponse)
                .toList())
            .build();
    }

    @Named("toCreateBankAccountResponse")
    default CreateBankAccountResponse toCreateBankAccountResponse(BankingEntity bankAccount, BankingEntity accountHolder){
        return CreateBankAccountResponse.builder()
            .bankAccount(toBankAccountDetailsResponse(bankAccount, List.of(accountHolder)))
            .build();
    }
}

