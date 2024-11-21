package com.jcondotta.web.controller;

import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.bank_account.FindBankAccountService;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class FindBankAccountControllerErrorIT implements LocalStackTestContainer {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    @Inject
    RequestSpecification requestSpecification;

    @Inject
    FindBankAccountService findBankAccountService;

    @MockBean(FindBankAccountService.class)
    FindBankAccountService findBankAccountService() {
        return Mockito.mock(FindBankAccountService.class);
    }

    @BeforeEach
    void beforeEach(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification
                .basePath(BankAccountURIBuilder.BANK_ACCOUNT_API_V1_MAPPING)
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn500InternalServerError_whenUnexpectedExceptionOccurs() {
        when(findBankAccountService.findBankAccountById(BANK_ACCOUNT_ID_BRAZIL))
                .thenThrow(new RuntimeException("Simulated unexpected exception"));

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo("An unexpected error occurred. Please try again later."));

        verify(findBankAccountService).findBankAccountById(BANK_ACCOUNT_ID_BRAZIL);
    }
}
