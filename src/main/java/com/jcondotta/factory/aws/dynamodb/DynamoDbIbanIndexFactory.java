package com.jcondotta.factory.aws.dynamodb;

import com.jcondotta.domain.BankingEntity;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Factory
public class DynamoDbIbanIndexFactory {

    @Singleton
    public DynamoDbIndex<BankingEntity> bankAccountIbanGSI(DynamoDbTable<BankingEntity> bankingEntityTable) {
        return bankingEntityTable.index("bank-account-iban-gsi");
    }
}
