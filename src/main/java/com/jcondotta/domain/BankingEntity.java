package com.jcondotta.domain;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
@DynamoDbBean
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
    private LocalDateTime dateOfOpening;
    private UUID accountHolderId;
    private String accountHolderName;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private AccountHolderType accountHolderType;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @DynamoDbAttribute("entityType")
    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
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

    @DynamoDbAttribute("accountHolderId")
    public UUID getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(UUID accountHolderId) {
        this.accountHolderId = accountHolderId;
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

    public static BankingEntity buildBankAccount(UUID bankAccountId, String iban, LocalDateTime dateOfOpening) {
        BankingEntity entity = new BankingEntity();
        entity.setPartitionKey(BANK_ACCOUNT_PK_TEMPLATE.formatted(bankAccountId));
        entity.setSortKey(BANK_ACCOUNT_SK_TEMPLATE.formatted(bankAccountId));
        entity.setEntityType(EntityType.BANK_ACCOUNT);
        entity.setBankAccountId(bankAccountId);
        entity.setIban(iban);
        entity.setDateOfOpening(dateOfOpening);
        return entity;
    }

    public static BankingEntity buildAccountHolder(UUID accountHolderId, String accountHolderName, String passportNumber, LocalDate dateOfBirth, AccountHolderType accountHolderType, UUID bankAccountId) {
        BankingEntity entity = new BankingEntity();
        entity.setPartitionKey(ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId));
        entity.setSortKey(ACCOUNT_HOLDER_SK_TEMPLATE.formatted(accountHolderId));
        entity.setEntityType(EntityType.ACCOUNT_HOLDER);
        entity.setAccountHolderId(accountHolderId);
        entity.setAccountHolderName(accountHolderName);
        entity.setPassportNumber(passportNumber);
        entity.setDateOfBirth(dateOfBirth);
        entity.setAccountHolderType(accountHolderType);
        entity.setBankAccountId(bankAccountId);
        return entity;
    }

    @Override
    public String toString() {
        return "BankingEntity{" +
                "PK='" + partitionKey + '\'' +
                ", SK='" + sortKey + '\'' +
                ", entityType=" + entityType +
                ", bankAccountId=" + bankAccountId +
                ", iban='" + iban + '\'' +
                ", dateOfOpening=" + dateOfOpening +
                ", accountHolderId=" + accountHolderId +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", accountHolderType=" + accountHolderType +
                '}';
    }
}
