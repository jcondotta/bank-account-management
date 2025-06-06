package com.jcondotta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankingEntity {

    public static final String BANK_ACCOUNT_PK_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String BANK_ACCOUNT_SK_TEMPLATE = "BANK_ACCOUNT#%s";

    public static final String ACCOUNT_HOLDER_PK_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String ACCOUNT_HOLDER_SK_TEMPLATE = "ACCOUNT_HOLDER#%s";

    private String partitionKey;
    private String sortKey;
    private EntityType entityType;
    private UUID bankAccountId;
    private String iban;
    private UUID accountHolderId;
    private String accountHolderName;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private AccountHolderType accountHolderType;
    private LocalDateTime createdAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("partitionKey")
    public String getPartitionKey() {
        return partitionKey;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("sortKey")
    public String getSortKey() {
        return sortKey;
    }

    @DynamoDbAttribute("entityType")
    public EntityType getEntityType() {
        return entityType;
    }

    @DynamoDbAttribute("bankAccountId")
    public UUID getBankAccountId() {
        return bankAccountId;
    }

    @DynamoDbAttribute("iban")
    public String getIban() {
        return iban;
    }

    @DynamoDbAttribute("accountHolderId")
    public UUID getAccountHolderId() {
        return accountHolderId;
    }

    @DynamoDbAttribute("accountHolderName")
    public String getAccountHolderName() {
        return accountHolderName;
    }

    @DynamoDbAttribute("passportNumber")
    public String getPassportNumber() {
        return passportNumber;
    }

    @DynamoDbAttribute("dateOfBirth")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @DynamoDbAttribute("accountHolderType")
    public AccountHolderType getAccountHolderType() {
        return accountHolderType;
    }

    @DynamoDbAttribute("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "BankingEntity{" +
                "partitionKey='" + partitionKey + '\'' +
                ", sortKey='" + sortKey + '\'' +
                ", entityType=" + entityType +
                ", bankAccountId=" + bankAccountId +
                ", iban='" + iban + '\'' +
                ", createdAt=" + createdAt +
                ", accountHolderId=" + accountHolderId +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", accountHolderType=" + accountHolderType +
                '}';
    }
}