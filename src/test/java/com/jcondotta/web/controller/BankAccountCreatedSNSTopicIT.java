package com.jcondotta.web.controller;

//@TestInstance(Lifecycle.PER_CLASS)
//@MicronautTest(transactional = false)
class BankAccountCreatedSNSTopicIT  {

//    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
//    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
//    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();
//
//    @Inject
//    SerializationService serializationService;
//
//    @Inject
//    RequestSpecification requestSpecification;
//
//    @Inject
//    SnsClient snsClient;
//
//    @Inject
//    AccountHolderCreatedSNSTopicConfig snsTopicConfig;
//
//    @Inject
//    SNSSubscriptionService snsSubscriptionService;
//
//    @Inject
//    SqsClient sqsClient;
//
//    @Inject
//    AccountHolderCreatedSQSQueueConfig sqsQueueConfig;
//    String bankAccountCreatedSQSQueueARN;
//    String sqsQueueSubscriptionARN;
//
//    @Inject
//    BankAccountURIConfiguration bankAccountURIConfig;
//
//    @BeforeEach
//    void beforeEach(RequestSpecification requestSpecification) {
//        this.requestSpecification = requestSpecification
//                .basePath(bankAccountURIConfig.rootPath())
//                .contentType(ContentType.JSON);
//
//        this.bankAccountCreatedSQSQueueARN = getQueueArn(sqsClient, sqsQueueConfig.queueURL());
//        var subscribeResponse = snsSubscriptionService.subscribeQueueToTopic(snsTopicConfig.topicArn(), bankAccountCreatedSQSQueueARN);
//
//        sqsQueueSubscriptionARN = subscribeResponse.subscriptionArn();
//    }
//
//    @AfterEach
//    void afterEach(){
//        snsClient.unsubscribe(builder -> builder
//                .subscriptionArn(sqsQueueSubscriptionARN)
//                .build());
//    }
//
//    @Test
//    void shouldPublishMessageSuccessfullyToSNSTopic_whenBankAccountIsCreated() {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//
//        var bankAccountDTO = given()
//            .spec(requestSpecification)
//                .body(serializationService.serialize(accountHolderRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.CREATED.getCode())
//                .extract()
//                    .response()
//                        .as(BankAccountDTO.class);
//
//        var messages = sqsClient.receiveMessage(builder -> builder.queueUrl(sqsQueueConfig.queueURL())
//                        .waitTimeSeconds(3)
//                        .build())
//                .messages();
//
//        assertThat(messages).hasSize(1)
//                .first()
//                .satisfies(message -> {
//                    var snsMessageWrapper = serializationService.deserialize(message.body(), JsonNode.class);
//                    var rawMessage = snsMessageWrapper.get("Message").getStringValue();
//
//                    var notification = serializationService.deserialize(rawMessage, AccountHolderCreatedNotification.class);
//                    assertThat(notification.bankAccountId()).isEqualTo(bankAccountDTO.getBankAccountId());
//
//                    bankAccountDTO.getPrimaryAccountHolder().ifPresent(accountHolderDTO -> {
//                        assertThat(notification.accountHolderId()).isEqualTo(accountHolderDTO.getAccountHolderId());
//                        assertThat(notification.accountHolderName()).isEqualTo(accountHolderDTO.getAccountHolderName());
//                    });
//                });
//    }
//
//    private String getQueueArn(SqsClient sqsClient, String queueUrl) {
//        return sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder()
//                        .queueUrl(queueUrl)
//                        .attributeNames(QueueAttributeName.QUEUE_ARN)
//                        .build())
//                .attributes()
//                .get(QueueAttributeName.QUEUE_ARN);
//    }
}
