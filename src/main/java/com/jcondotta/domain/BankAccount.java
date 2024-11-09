package com.jcondotta.domain;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
@DynamoDbBean
public class BankAccount {

    private UUID bankAccountId;

    private String accountHolderName;
    private String passportNumber;
    private LocalDate dateOfBirth;

    private String iban;
    private LocalDateTime dateOfOpening;


    public BankAccount() {}

    public BankAccount(UUID bankAccountId, AccountHolder accountHolder, String iban, LocalDateTime dateOfOpening) {
        this.bankAccountId = bankAccountId;
        this.accountHolderName = accountHolder.getAccountHolderName();
        this.dateOfBirth = accountHolder.getDateOfBirth();
        this.passportNumber = accountHolder.getPassportNumber();
        this.iban = iban;
        this.dateOfOpening = dateOfOpening;
    }

    @DynamoDbPartitionKey
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

    @DynamoDbAttribute("dateOfBirth")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @DynamoDbAttribute("passportNumber")
    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
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
