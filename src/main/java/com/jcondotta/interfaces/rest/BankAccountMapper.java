package com.jcondotta.interfaces.rest;

import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountRequest;
import com.jcondotta.interfaces.rest.bankaccount.CreateBankAccountResponse;
import com.jcondotta.interfaces.rest.accountholder.CreateJointAccountHolderResponse;
import com.jcondotta.interfaces.rest.lookup.AccountHolderLookupResponse;
import com.jcondotta.interfaces.rest.lookup.BankAccountLookupResponse;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.domain.shared.valueobjects.CurrencyValue;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderTypeValue;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import net.datafaker.Faker;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    Logger LOGGER = LoggerFactory.getLogger(BankAccountMapper.class);

    default BankAccount toBankAccount(CreateBankAccountRequest request, @Context Clock clock) {
        var fixedClock = Clock.fixed(Instant.now(clock), ZoneOffset.UTC);

        return new BankAccount(
            BankAccountId.of(UUID.randomUUID()),
            AccountTypeValue.of(request.accountType()),
            CurrencyValue.of(request.currency()),
            Iban.of(new Faker().finance().iban()),
            AccountStatusValue.of(AccountStatus.PENDING),
            CreatedAt.now(fixedClock),
            List.of(
                toPrimaryAccountHolder(request.accountHolder(), fixedClock)
            ));
    }

    default AccountHolder toPrimaryAccountHolder(AccountHolderDetailsRequest request, @Context Clock clock) {
        return toAccountHolder(request, AccountHolderType.PRIMARY, clock);
    }

    default AccountHolder toJointAccountHolder(AccountHolderDetailsRequest request, @Context Clock clock) {
        return toAccountHolder(request, AccountHolderType.JOINT, clock);
    }

    private AccountHolder toAccountHolder(AccountHolderDetailsRequest request, AccountHolderType accountHolderType, @Context Clock clock) {
        return new AccountHolder(
            AccountHolderId.of(UUID.randomUUID()),
            AccountHolderName.of(request.accountHolderName()),
            request.passportNumber(),
            DateOfBirth.of(request.dateOfBirth(), clock),
            AccountHolderTypeValue.of(accountHolderType),
            CreatedAt.now(clock)
        );
    }

    default BankAccountDetailsResponse toBankAccountDetailsResponse(BankAccount bankAccount) {
        return BankAccountDetailsResponse.builder()
            .bankAccountId(bankAccount.bankAccountId().value())
            .accountType(bankAccount.accountType().value())
            .currency(bankAccount.currency().value())
            .iban(bankAccount.iban().value())
            .status(bankAccount.status().value())
            .dateOfOpening(bankAccount.createdAt().value())
            .accountHolders(bankAccount.accountHolders().stream()
                .map(this::toAccountHolderDetailsResponse)
                .toList())
            .build();
    }

    default BankAccountLookupResponse toBankAccountLookupResponse(BankAccount bankAccount){
        return BankAccountLookupResponse.builder()
            .bankAccount(toBankAccountDetailsResponse(bankAccount))
            .build();
    }

    default AccountHolderDetailsResponse toAccountHolderDetailsResponse(AccountHolder accountHolder){
        return AccountHolderDetailsResponse.builder()
            .accountHolderId(accountHolder.accountHolderId().value())
            .accountHolderName(accountHolder.accountHolderName().value())
            .passportNumber(accountHolder.passportNumber())
            .dateOfBirth(accountHolder.dateOfBirth().value())
            .accountHolderType(accountHolder.accountHolderType().value())
            .createdAt(accountHolder.createdAt().value())
            .build();
    }

    default CreateJointAccountHolderResponse toCreateJointAccountHolderResponse(AccountHolder accountHolder) {
        return CreateJointAccountHolderResponse.builder()
            .accountHolder(toAccountHolderDetailsResponse(accountHolder))
            .build();
    }

    default AccountHolderLookupResponse toAccountHolderLookupResponse(AccountHolder accountHolder) {
        return AccountHolderLookupResponse.builder()
            .accountHolder(toAccountHolderDetailsResponse(accountHolder))
            .build();
    }

    default CreateBankAccountResponse toCreateBankAccountResponse(BankAccount bankAccount) {
        return CreateBankAccountResponse.builder()
            .bankAccount(toBankAccountDetailsResponse(bankAccount))
            .build();
    }
}