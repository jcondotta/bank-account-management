package com.jcondotta.event;

//@ExtendWith(MockitoExtension.class)
class AccountHolderCreatedSNSTopicPublisherTest {

//    private static final String BANK_ACCOUNT_CREATED_TOPIC_ARN = "arn:aws:sns:us-east-1:123456789012:bank-account-created-topic";
//
//    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
//    private static final String MESSAGE_ID = "message-id-12345";
//
//    @Mock
//    private SnsClient snsClient;
//
//    @Mock
//    private AccountHolderCreatedSNSTopicConfig snsTopicConfig;
//
//    private AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;
//
//    private SerializationService serializationService;
//
//    private PublishResponse publishResponse = PublishResponse.builder().messageId(MESSAGE_ID).build();
//
//    @BeforeEach
//    void beforeEach() {
//        serializationService = new SerializationService(JsonMapper.createDefault());
//        snsTopicPublisher = new AccountHolderCreatedSNSTopicPublisher(snsClient, snsTopicConfig, serializationService);
//    }
//
//    @AfterEach
//    void afterEach(){
//        assertThat(MDC.get("bankAccountId"))
//                .as("MDC should be cleared after the publishMessage method completes for bankAccountId")
//                .isNull();
//        assertThat(MDC.get("accountHolderId"))
//                .as("MDC should be cleared after the publishMessage method completes for accountHolderId")
//                .isNull();
//    }
//
//    @ParameterizedTest
//    @EnumSource(AccountHolderType.class)
//    void shouldPublishMessage_whenAccountHolderDTOIsValid(AccountHolderType accountHolderType){
//        when(snsTopicConfig.topicArn()).thenReturn(BANK_ACCOUNT_CREATED_TOPIC_ARN);
//        when(snsClient.publish(any(PublishRequest.class))).thenReturn(publishResponse);
//
//        var accountHolder = TestAccountHolderFactory.create(
//                TestAccountHolderRequest.JEFFERSON,
//                accountHolderType,
//                BANK_ACCOUNT_ID_BRAZIL
//        );
//
//        var accountHolderDTO = new AccountHolderDTO(accountHolder);
//        var returnedMessageId = snsTopicPublisher.publishMessage(accountHolderDTO);
//        assertThat(returnedMessageId).isEqualTo(MESSAGE_ID);
//
//        var publishRequestCaptor = ArgumentCaptor.forClass(PublishRequest.class);
//        verify(snsClient).publish(publishRequestCaptor.capture());
//
//        var capturedRequest = publishRequestCaptor.getValue();
//        assertThat(capturedRequest.topicArn()).isEqualTo(BANK_ACCOUNT_CREATED_TOPIC_ARN);
//
//        var expectedNotification = new AccountHolderCreatedNotification(
//                accountHolder.getAccountHolderId(),
//                accountHolder.getAccountHolderName(),
//                BANK_ACCOUNT_ID_BRAZIL
//        );
//        try {
//            assertThat(capturedRequest.message()).isEqualTo(JsonMapper.createDefault().writeValueAsString(expectedNotification));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        verify(snsTopicConfig, times(2)).topicArn();
//    }
}
