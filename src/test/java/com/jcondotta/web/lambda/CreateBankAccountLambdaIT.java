package com.jcondotta.web.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.domain.BankingEntity;
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
import io.micronaut.json.tree.JsonNode;
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
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        requestEvent.setBody(jsonMapper.writeValueAsString(createBankAccountRequest));

        var response = requestEventFunction.handleRequest(requestEvent, mockLambdaContext);

        assertThat(response.getStatusCode())
                .as("Verify the response has the correct status code")
                .isEqualTo(HttpStatus.CREATED.getCode());

        var bankAccount = jsonMapper.readValue(response.getBody(), BankingEntity.class);
        assertAll(
                () -> assertThat(bankAccount.getBankAccountId()).isNotNull()
        );
    }

    @Test
    void shouldReturn400BadRequest_whenAccountHolderIsNull() throws IOException {
        var createBankAccountRequest = new CreateBankAccountRequest(null);
        requestEvent.setBody(jsonMapper.writeValueAsString(createBankAccountRequest));

        var response = requestEventFunction.handleRequest(requestEvent, mockLambdaContext);

        assertThat(response.getStatusCode())
                .as("Verify the response has the correct status code")
                .isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        var responseBodyJSON = jsonMapper.readValue(response.getBody(), JsonNode.class);
        var errorMessage = responseBodyJSON.get("_embedded").get("errors")
                .get(0).get("message")
                .coerceStringValue();

        var expectedMessageKey = "bankAccount.accountHolder.notNull";
        assertThat(errorMessage)
                .as("Verify the error message in the response body")
                .isEqualTo(messageSource.getMessage(expectedMessageKey, Locale.getDefault())
                        .orElseThrow(() -> new IllegalArgumentException("Message not found for key: " + expectedMessageKey)));
    }
}

