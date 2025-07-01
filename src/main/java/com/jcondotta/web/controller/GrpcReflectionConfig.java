package com.jcondotta.web.controller;

import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcReflectionConfig {

    @Bean
    public io.grpc.BindableService grpcReflectionService() {
        return ProtoReflectionService.newInstance();
    }
}