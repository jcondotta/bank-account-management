package com.jcondotta.web.controller;

import com.jcondotta.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcQuickTest {
//    public static void main(String[] args) {
//        ManagedChannel channel = ManagedChannelBuilder
//            .forAddress("localhost", 9090)
//            .usePlaintext()
//            .build();
//
//        BankAccountServiceGrpc.BankAccountServiceBlockingStub stub =
//            BankAccountServiceGrpc.newBlockingStub(channel);
//
//        LookupRequest request = LookupRequest.newBuilder()
//            .setBankAccountId("f47ac10b-58cc-4372-a567-0e02b2c3d479")
//            .build();
//
//        LookupResponse response = stub.lookup(request);
//
//        System.out.println("✅ Response: " + response.getIban());
//
//        channel.shutdown();
//    }
}
