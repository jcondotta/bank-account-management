package com.jcondotta.web.controller.bank_account;

public class BankAccountGrpcController {//extends BankAccountServiceGrpc.BankAccountServiceImplBase {

//    private final BankAccountLookupUseCase useCase;
//
//    public BankAccountGrpcController(BankAccountLookupUseCase useCase) {
//        this.useCase = useCase;
//    }
//
//    @Override
//    public void lookup(BankAccountLookupGPRCRequest request, StreamObserver<BankAccountLookupGPRCResponse> responseObserver) {
//        try {
////            var bankAccountId = new BankAccountId(request.getBankAccountId());
////            var response = BankAccountGrpcMapper.toLookupResponse(useCase.lookup(bankAccountId).bankAccount());
//
//            responseObserver.onNext(null);
//            responseObserver.onCompleted();
//
//        } catch (IllegalArgumentException e) {
//            responseObserver.onError(Status.INVALID_ARGUMENT
//                .withDescription("Invalid UUID format: " + e.getMessage())
//                .asRuntimeException());
//        } catch (Exception e) {
//            responseObserver.onError(Status.INTERNAL
//                .withDescription("Unexpected error")
//                .withCause(e)
//                .asRuntimeException());
//        }
//    }
}
