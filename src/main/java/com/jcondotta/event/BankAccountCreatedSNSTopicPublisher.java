package com.jcondotta.event;

import com.jcondotta.configuration.BankAccountCreatedSNSTopicConfig;
import com.jcondotta.service.dto.BankAccountDTO;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.io.IOException;
import java.util.Optional;

@Singleton
public class BankAccountCreatedSNSTopicPublisher {

    private final SnsClient snsClient;
    private final BankAccountCreatedSNSTopicConfig bankAccountCreatedSNSTopicConfig;
    private final JsonMapper jsonMapper;

    @Inject
    public BankAccountCreatedSNSTopicPublisher(SnsClient snsClient, BankAccountCreatedSNSTopicConfig bankAccountCreatedSNSTopicConfig, JsonMapper jsonMapper) {
        this.snsClient = snsClient;
        this.bankAccountCreatedSNSTopicConfig = bankAccountCreatedSNSTopicConfig;
        this.jsonMapper = jsonMapper;
    }

    public String publishMessage(BankAccountDTO bankAccountDTO) {
        Optional<String> snsTopicBankAccountCreated = snsClient.listTopics().topics()
                .stream()
                .map(snsTopic -> snsTopic.topicArn())
                .filter(topicArn -> topicArn.endsWith(":" + bankAccountCreatedSNSTopicConfig.topicName()))
                .findFirst();

        if(snsTopicBankAccountCreated.isPresent()){
            var publishRequest = PublishRequest.builder()
                .topicArn(snsTopicBankAccountCreated.get())
                .message(asdasdsa(bankAccountDTO))
                .subject("New Bank Account Created")
                .build();

            snsClient.publish(publishRequest);
        }

        return "ufa";
    }

    private String asdasdsa(BankAccountDTO bankAccountDTO){
        try {
            return jsonMapper.writeValueAsString(bankAccountDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
