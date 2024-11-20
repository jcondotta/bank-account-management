package com.jcondotta.web.controller;

import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.dto.AccountHoldersDTO;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.context.MessageSource;
import io.micronaut.http.HttpStatus;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.MDC;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class CreateJointAccountHoldersControllerIT implements LocalStackTestContainer {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    @Inject
    Clock testClockUTC;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    RequestSpecification requestSpecification;

    @Inject
    @Named("exceptionMessageSource")
    MessageSource messageSource;

    public static Supplier<IllegalArgumentException> messageKeyNotFoundException(String messageKey) {
        return () -> new IllegalArgumentException("Message not found for key: " + messageKey);
    }

    @BeforeEach
    void beforeEach(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification
                .basePath(BankAccountURIBuilder.BANK_ACCOUNT_API_V1_MAPPING)
                .contentType(ContentType.JSON);
    }

    @AfterEach
    void afterEach(){
        assertThat(MDC.get("bankAccountId"))
                .as("MDC should be cleared after the publishMessage method completes for bankAccountId")
                .isNull();
        assertThat(MDC.get("accountHolderId"))
                .as("MDC should be cleared after the publishMessage method completes for accountHolderId")
                .isNull();
    }

    @Test
    void shouldReturn201CreatedWithValidLocationHeader_whenRequestHasOneAccountHolder() throws IOException {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var accountHolders = List.of(jeffersonAccountHolderRequest);

        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolders);

        var response = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(createJointAccountHoldersRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.getCode())
                .extract();

        var expectedLocation = BankAccountURIBuilder.bankAccountURI(BANK_ACCOUNT_ID_BRAZIL);
        assertThat(response.header("location")).isEqualTo(expectedLocation.getRawPath());

        var accountHoldersDTO = response.as(AccountHoldersDTO.class);
        assertThat(accountHoldersDTO.accountHolders()).hasSize(1);
    }

    @Test
    void shouldReturn201Created_whenRequestHasTwoAccountHolders() throws IOException {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var virginioAccountHolderRequest = TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest();
        var accountHolders = List.of(jeffersonAccountHolderRequest, virginioAccountHolderRequest);

        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(accountHolders);

        var accountHoldersDTO = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(createJointAccountHoldersRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.getCode())
                .extract()
                    .as(AccountHoldersDTO.class);

        assertThat(accountHoldersDTO.accountHolders())
            .hasSize(2)
            .satisfies(entities -> assertAll(
                    () -> {
                        var jeffersonEntity = entities.get(0);
                        assertThat(jeffersonEntity.getAccountHolderId()).isNotNull();
                        assertThat(jeffersonEntity.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName());
                        assertThat(jeffersonEntity.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber());
                        assertThat(jeffersonEntity.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth());
                        assertThat(jeffersonEntity.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
                    },
                    () -> {
                        var virginioEntity = entities.get(1);
                        assertThat(virginioEntity.getAccountHolderId()).isNotNull();
                        assertThat(virginioEntity.getAccountHolderName()).isEqualTo(virginioAccountHolderRequest.accountHolderName());
                        assertThat(virginioEntity.getPassportNumber()).isEqualTo(virginioAccountHolderRequest.passportNumber());
                        assertThat(virginioEntity.getDateOfBirth()).isEqualTo(virginioAccountHolderRequest.dateOfBirth());
                        assertThat(virginioEntity.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
                    }
            )
        );
    }
}
