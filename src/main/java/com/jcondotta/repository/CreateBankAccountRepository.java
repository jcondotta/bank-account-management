package com.jcondotta.repository;

import com.jcondotta.domain.BankAccount;
import com.jcondotta.service.dto.BankAccountDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.function.Supplier;

@Singleton
public class CreateBankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountRepository.class);

    private final DynamoDbTable<BankAccount> dynamoDbTable;

    @Inject
    public CreateBankAccountRepository(DynamoDbTable<BankAccount> dynamoDbTable) {
        this.dynamoDbTable = dynamoDbTable;
    }

    public CreateBankAccountResponse create(Supplier<BankAccount> bankAccountSupplier) {
        var bankAccount = bankAccountSupplier.get();

        dynamoDbTable.putItem(bankAccount);

        LOGGER.info("[BankAccountId={}] Bank account saved to DB", bankAccount.getBankAccountId());

        return CreateBankAccountResponse.builder(new BankAccountDTO(bankAccount))
                .isIdempotent(false)
                .build();
    }
}
