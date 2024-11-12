package com.jcondotta.domain;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
@DynamoDbBean
public class BankAccount {

    public static final String PARTITION_KEY_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String SORT_KEY_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String ENTITY_TYPE = "BankAccount";

    private String pk;
    private String sk;
    private String entityType;
    private UUID bankAccountId;
    private String iban;
    private LocalDateTime dateOfOpening;

    public BankAccount() {}

    public BankAccount(UUID bankAccountId, String iban, LocalDateTime dateOfOpening) {
        this.pk = PARTITION_KEY_TEMPLATE.formatted(bankAccountId);
        this.sk = SORT_KEY_TEMPLATE.formatted(bankAccountId);
        this.entityType = ENTITY_TYPE;
        this.bankAccountId = bankAccountId;
        this.iban = iban;
        this.dateOfOpening = dateOfOpening;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    @DynamoDbAttribute("entityType")
    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @DynamoDbAttribute("bankAccountId")
    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @DynamoDbAttribute("iban")
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @DynamoDbAttribute("dateOfOpening")
    public LocalDateTime getDateOfOpening() {
        return dateOfOpening;
    }

    public void setDateOfOpening(LocalDateTime dateOfOpening) {
        this.dateOfOpening = dateOfOpening;
    }
}
