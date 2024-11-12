package com.jcondotta.repository;

import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.BankAccount;
import com.jcondotta.service.dto.BankAccountDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Singleton
public class CreateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankAccount> bankAccountTable;
    private final DynamoDbTable<AccountHolder> accountHolderTable;

    @Inject
    public CreateBankAccountRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                       DynamoDbTable<BankAccount> bankAccountTable,
                                       DynamoDbTable<AccountHolder> accountHolderTable) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.bankAccountTable = bankAccountTable;
        this.accountHolderTable = accountHolderTable;
    }

    public CreateBankAccountResponse create(BankAccount bankAccount, AccountHolder accountHolder) {
        try {
            MDC.put("bankAccountId", bankAccount.getBankAccountId().toString());
            MDC.put("accountHolderId", accountHolder.getAccountHolderId().toString());
            MDC.put("accountHolderName", accountHolder.getAccountHolderName());

            LOGGER.debug("Initiating transaction to create bank account and account holder.");


//            bankAccountTable.putItem(bankAccount);
//            accountHolderTable.putItem(accountHolder);
            var transactWriteRequest = TransactWriteItemsEnhancedRequest.builder()
                    .addPutItem(bankAccountTable, bankAccount)
                    .addPutItem(accountHolderTable, accountHolder)
                    .build();
//
            dynamoDbEnhancedClient.transactWriteItems(transactWriteRequest);

            LOGGER.info("Successfully saved bank account and primary account holder to DB.");

            return CreateBankAccountResponse.builder(new BankAccountDTO(bankAccount, accountHolder))
                    .isIdempotent(false)
                    .build();
        }
        catch (DynamoDbException e) {
            LOGGER.error("Error creating bank account and account holder: {}", e.getMessage(), e);
            throw e;
        }
        finally {
            MDC.clear();
        }
    }
}
