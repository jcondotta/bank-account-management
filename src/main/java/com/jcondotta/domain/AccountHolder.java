package com.jcondotta.domain;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDate;
import java.util.UUID;

@Serdeable
@DynamoDbBean
public class AccountHolder {

    public static final String PARTITION_KEY_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String SORT_KEY_TEMPLATE = "ACCOUNT_HOLDER#%s";
    public static final String ENTITY_TYPE = "BankAccount";

    private String pk;
    private String sk;
    private String entityType;
    private UUID accountHolderId;
    private UUID bankAccountId;
    private String accountHolderName;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private AccountHolderType accountHolderType;

    public AccountHolder() {}

    public AccountHolder(UUID bankAccountId, UUID accountHolderId, String accountHolderName, String passportNumber, LocalDate dateOfBirth, AccountHolderType accountHolderType) {
        this.pk = PARTITION_KEY_TEMPLATE.formatted(bankAccountId);
        this.sk = SORT_KEY_TEMPLATE.formatted(accountHolderId);
        this.entityType = ENTITY_TYPE;
        this.bankAccountId = bankAccountId;
        this.accountHolderId = accountHolderId;
        this.accountHolderName = accountHolderName;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.accountHolderType = accountHolderType;
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

    @DynamoDbAttribute("accountHolderId")
    public UUID getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(UUID accountHolderId) {
        this.accountHolderId = accountHolderId;
    }

    @DynamoDbAttribute("bankAccountId")
    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @DynamoDbAttribute("accountHolderName")
    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    @DynamoDbAttribute("passportNumber")
    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @DynamoDbAttribute("dateOfBirth")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @DynamoDbAttribute("accountHolderType")
    public AccountHolderType getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(AccountHolderType accountHolderType) {
        this.accountHolderType = accountHolderType;
    }
}
