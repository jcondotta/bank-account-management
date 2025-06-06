package com.jcondotta.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public enum TestBankAccountId {

    BRAZIL(UUID.fromString("01920bff-1338-7efd-ade6-e9128debe5d4")),
    ITALY(UUID.fromString("01921f7f-5672-70ac-8c7e-6d7a941706cb"));

    private final UUID bankAccountId;

}