package com.jcondotta.web.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.domain.BankAccount;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateBankAccountRequest;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.MessageSource;
import io.micronaut.function.aws.proxy.MockLambdaContext;
import io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class CreateBankAccountLambdaIT implements LocalStackTestContainer {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Context mockLambdaContext = new MockLambdaContext();

    private ApiGatewayProxyRequestEventFunction requestEventFunction;
    private APIGatewayProxyRequestEvent requestEvent;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    ApplicationContext applicationContext;

    @Inject
    @Named("exceptionMessageSource")
    MessageSource messageSource;

    @BeforeAll
    void beforeAll() {
        requestEventFunction = new ApiGatewayProxyRequestEventFunction(applicationContext);
    }

    @BeforeEach
    void beforeEach() {
        requestEvent = new APIGatewayProxyRequestEvent()
                .withPath(BankAccountURIBuilder.BASE_PATH_API_V1_MAPPING)
                .withHttpMethod(HttpMethod.POST.name())
                .withHeaders(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturn201Created_whenRequestIsValid() throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        requestEvent.setBody(jsonMapper.writeValueAsString(addBankAccountRequest));

        var response = requestEventFunction.handleRequest(requestEvent, mockLambdaContext);

        assertThat(response.getStatusCode())
                .as("Verify the response has the correct status code")
                .isEqualTo(HttpStatus.CREATED.getCode());

        var bankAccount = jsonMapper.readValue(response.getBody(), BankAccount.class);
        assertAll(
                () -> assertThat(bankAccount.getBankAccountId()).isNotNull()
        );
    }

    @Test
    void shouldReturn400BadRequest_whenAccountHolderIsNull() throws IOException {
        var addBankAccountRequest = new CreateBankAccountRequest(null);
        requestEvent.setBody(jsonMapper.writeValueAsString(addBankAccountRequest));

        var response = requestEventFunction.handleRequest(requestEvent, mockLambdaContext);

        assertThat(response.getStatusCode())
                .as("Verify the response has the correct status code")
                .isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        var responseBodyJSON = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        var errorMessage = responseBodyJSON.at("/_embedded/errors/0/message").asText();

        assertThat(errorMessage)
                .as("Verify the error message in the response body")
                .isEqualTo(messageSource.getMessage("bankAccount.accountHolder.notNull", Locale.getDefault())
                        .orElseThrow());
    }
}

