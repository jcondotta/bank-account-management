package com.jcondotta.listener;

import com.jcondotta.domain.BankAccount;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Singleton
public class DynamoDBTableBankAccountEventListener implements BeanCreatedEventListener<DynamoDbTable<BankAccount>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBTableBankAccountEventListener.class);

    @Override
    public DynamoDbTable<BankAccount> onCreated(@NonNull BeanCreatedEvent<DynamoDbTable<BankAccount>> event) {
        var dynamoDBTable = event.getBean();

        try {
            dynamoDBTable.describeTable();
            LOGGER.info("DynamoDB table for type {} exists.", dynamoDBTable.tableSchema().itemType());
        }
        catch (ResourceNotFoundException e) {
            LOGGER.warn("DynamoDB table for type {} not found. Creating the table.", dynamoDBTable.tableSchema().itemType());
            dynamoDBTable.createTable();
        }
        catch (Exception e) {
            LOGGER.error("An unexpected error occurred while checking the DynamoDB table: {}", e.getMessage(), e);
        }

        return dynamoDBTable;
    }
}