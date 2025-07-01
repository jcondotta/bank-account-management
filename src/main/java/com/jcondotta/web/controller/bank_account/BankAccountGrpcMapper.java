package com.jcondotta.web.controller.bank_account;

public class BankAccountGrpcMapper {

//    public static BankAccountLookupGPRCResponse toLookupResponse(BankAccountDetailsResponse details) {
//        return BankAccountLookupGPRCResponse.newBuilder()
//            .setBankAccountId(details.getBankAccountId().toString())
//            .setAccountType(details.getAccountType().name())
//            .setCurrency(details.getCurrency().name())
//            .setIban(details.getIban())
//            .setDateOfOpening(details.getDateOfOpening().toString())
//            .setStatus(details.getStatus().name())
//            .addAllAccountHolders(details.getAccountHolders().stream()
//                .map(BankAccountGrpcMapper::toAccountHolder)
//                .toList())
//            .build();
//    }
//
//    public static AccountHolder toAccountHolder(AccountHolderDetailsResponse holder) {
//        return AccountHolder.newBuilder()
//            .setId(holder.getAccountHolderId().toString())
//            .setName(holder.getAccountHolderName())
//            .setDateOfBirth(holder.getDateOfBirth().toString())
//            .setPassportNumber(holder.getPassportNumber())
//            .setAccountHolderType(holder.getAccountHolderType().name())
//            .setCreatedAt(holder.getCreatedAt().toString())
//            .build();
//    }
}
