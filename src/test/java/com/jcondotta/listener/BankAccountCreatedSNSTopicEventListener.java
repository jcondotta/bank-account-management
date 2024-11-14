package com.jcondotta.listener;

import com.jcondotta.configuration.BankAccountCreatedSNSTopicConfig;
import com.jcondotta.event.SNSTopicArnFinder;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sns.SnsClient;

@Singleton
public class BankAccountCreatedSNSTopicEventListener implements BeanCreatedEventListener<SnsClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountCreatedSNSTopicEventListener.class);

    @Inject
    BankAccountCreatedSNSTopicConfig bankAccountCreatedSNSTopicConfig;

    @Override
    public SnsClient onCreated(@NonNull BeanCreatedEvent<SnsClient> event) {
        var snsClient = event.getBean();
        var currentTopics = snsClient.listTopics().topics();

        if (currentTopics.stream().noneMatch(topic -> topic.topicArn().endsWith(bankAccountCreatedSNSTopicConfig.topicName()))) {
            LOGGER.info("Creating SNS topic with name: {}", bankAccountCreatedSNSTopicConfig.topicName());

            snsClient.createTopic(builder -> builder.name(bankAccountCreatedSNSTopicConfig.topicName()));
        }
        else {
            LOGGER.debug("SNS topic exists: {}", bankAccountCreatedSNSTopicConfig.topicName());
        }

        return snsClient;
    }
}