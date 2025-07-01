package com.jcondotta.web.controller.bank_account;

import com.jcondotta.application.usecase.accountholder.AccountHolderLookupUseCase;
import com.jcondotta.grpc.AccountHolder;
import com.jcondotta.grpc.AccountHolderLookupRequest;
import com.jcondotta.grpc.AccountHolderLookupResponse;
import com.jcondotta.grpc.AccountHolderServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class AccountHolderGrpcController extends AccountHolderServiceGrpc.AccountHolderServiceImplBase {

    private final AccountHolderLookupUseCase useCase;

    public AccountHolderGrpcController(AccountHolderLookupUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void lookup(AccountHolderLookupRequest request, StreamObserver<AccountHolderLookupResponse> responseObserver) {
        try {
//            var accountHolderId = new AccountHolderId(request.getAccountHolderId());
//            var bankAccountId = new BankAccountId(request.getBankAccountId());
//            var details = useCase.lookup(com.jcondotta.interfaces.rest.lookup.AccountHolderLookupRequest.builder()
//                    .accountHolderId(accountHolderId)
//                    .bankAccountId(bankAccountId)
//                .build());

            AccountHolder accountHolder = null;//com.jcondotta.web.controller.bank_account.BankAccountGrpcMapper.toAccountHolder(details.accountHolder());

            var response = AccountHolderLookupResponse.newBuilder()
                .setAccountHolder(accountHolder)
                .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("Invalid UUID format: " + e.getMessage())
                .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withDescription("Unexpected error")
                .asRuntimeException());
        }
    }
}
