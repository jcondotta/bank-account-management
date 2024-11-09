package com.jcondotta.domain;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDate;
import java.util.UUID;

@Serdeable
@DynamoDbBean
public class AccountHolder {

    private UUID accountHolderId;
    private String accountHolderName;
    private String passportNumber;
    private LocalDate dateOfBirth;

    public AccountHolder() {}

    public AccountHolder(UUID accountHolderId, String accountHolderName, String passportNumber, LocalDate dateOfBirth) {
        this.accountHolderId = accountHolderId;
        this.accountHolderName = accountHolderName;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
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
}
