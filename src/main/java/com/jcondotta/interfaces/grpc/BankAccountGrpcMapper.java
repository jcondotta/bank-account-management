package com.jcondotta.interfaces.grpc;

public class BankAccountGrpcMapper {

//    public static BankAccountLookupGPRCResponse toLookupResponse(BankAccountDetailsResponse details) {
//        return BankAccountLookupGPRCResponse.newBuilder()
//            .setBankAccountId(details.getBankAccountId().toString())
//            .setAccountType(details.getAccountType().accountHolderName())
//            .setCurrency(details.getCurrency().accountHolderName())
//            .setIban(details.getIban())
//            .setDateOfOpening(details.getDateOfOpening().toString())
//            .setStatus(details.getStatus().accountHolderName())
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
//            .setAccountHolderType(holder.getAccountHolderType().accountHolderName())
//            .setCreatedAt(holder.getCreatedAt().toString())
//            .build();
//    }
}
