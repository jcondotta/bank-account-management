package com.jcondotta.web.lambda;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@MicronautTest(transactional = false)
class CreateBankAccountLambdaIT {
        //implements LocalStackTestContainer {

//    private static final Context mockLambdaContext = new MockLambdaContext();
//
//    private ApiGatewayProxyRequestEventFunction requestEventFunction;
//    private APIGatewayProxyRequestEvent requestEvent;
//
//    @Inject
//    JsonMapper jsonMapper;
//
//    @Inject
//    ApplicationContext applicationContext;
//
//    @Inject
//    BankAccountURIConfiguration bankAccountURIConfiguration;
//
//    @BeforeAll
//    void beforeAll() {
//        requestEventFunction = new ApiGatewayProxyRequestEventFunction(applicationContext);
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        requestEvent = new APIGatewayProxyRequestEvent()
//                .withPath(bankAccountURIConfiguration.rootPath())
//                .withHttpMethod(HttpMethod.POST.name())
//                .withHeaders(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void shouldReturn201Created_whenRequestIsValid() throws IOException {
//        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//        requestEvent.setBody(jsonMapper.writeValueAsString(accountHolderRequest));
//
//        var response = requestEventFunction.handleRequest(requestEvent, mockLambdaContext);
//
//        assertThat(response.getStatusCode())
//                .as("Verify the response has the correct status code")
//                .isEqualTo(HttpStatus.CREATED.getCode());
//
//        var bankAccount = jsonMapper.readValue(response.getBody(), BankingEntity.class);
//        assertAll(
//                () -> assertThat(bankAccount.getBankAccountId()).isNotNull()
//        );
//    }
}

