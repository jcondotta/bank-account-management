package com.jcondotta.infrastructure.config.aws.dynamodb;

import com.jcondotta.configuration.BankingEntitiesDynamoDBTableConfig;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoDBBankingEntityTableFactory {

    @Bean
    public DynamoDbTable<BankingEntity> bankingEntityTable(
            DynamoDbEnhancedClient dynamoDbClient,
            BankingEntitiesDynamoDBTableConfig dynamoDBTableConfig) {

        var schema = TableSchema.fromBean(BankingEntity.class);
        return dynamoDbClient.table(dynamoDBTableConfig.tableName(), schema);
    }
}
