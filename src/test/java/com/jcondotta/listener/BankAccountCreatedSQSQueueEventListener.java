package com.jcondotta.listener;

import com.jcondotta.configuration.BankAccountCreatedSQSQueueConfig;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.SqsClient;

@Singleton
public class BankAccountCreatedSQSQueueEventListener implements BeanCreatedEventListener<SqsClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountCreatedSQSQueueEventListener.class);

    @Inject
    BankAccountCreatedSQSQueueConfig bankAccountCreatedSQSQueueConfig;

    @Override
    public SqsClient onCreated(@NonNull BeanCreatedEvent<SqsClient> event) {
        var sqsClient = event.getBean();

        var queueUrls = sqsClient.listQueues().queueUrls();
        var queueName = bankAccountCreatedSQSQueueConfig.queueName();

        if (queueUrls.stream().noneMatch(queueUrl -> queueUrl.contains(queueName))) {
            LOGGER.info("Queue not found. Creating SQS queue with name: {}", queueName);
            sqsClient.createQueue(builder -> builder.queueName(queueName));
        }
        else {
            LOGGER.debug("Queue exists: {}", queueName);
        }

        return sqsClient;
    }
}