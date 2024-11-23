package com.jcondotta.web.controller;

import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.bank_account.CreateJointAccountHolderService;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHolderRequest;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.context.MessageSource;
import io.micronaut.http.HttpStatus;
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

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class FindBankAccountControllerIT implements LocalStackTestContainer {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    @Inject
    Clock testClockUTC;

    @Inject
    CreateBankAccountService createBankAccountService;

    @Inject
    CreateJointAccountHolderService createJointAccountHolderService;

    @Inject
    RequestSpecification requestSpecification;

    @Inject
    @Named("exceptionMessageSource")
    MessageSource messageSource;

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
    void shouldReturn200Ok_whenBankAccountWithAccountHolderExists() {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createdBankAccountDTO = createBankAccountService.create(jeffersonAccountHolderRequest);

        var patrizioAccountHolderRequest = TestAccountHolderRequest.PATRIZIO.toAccountHolderRequest();
        var patrizioJointRequest = new CreateJointAccountHolderRequest(createdBankAccountDTO.getBankAccountId(), patrizioAccountHolderRequest);
        createJointAccountHolderService.create(patrizioJointRequest);

        var virginioAccountHolderRequest = TestAccountHolderRequest.VIRGINIO.toAccountHolderRequest();
        var virginioJointRequest = new CreateJointAccountHolderRequest(createdBankAccountDTO.getBankAccountId(), virginioAccountHolderRequest);
        createJointAccountHolderService.create(virginioJointRequest);

        var fetchedBankAccountDTO = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", createdBankAccountDTO.getBankAccountId())
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.getCode())
                .extract()
                    .response()
                        .as(BankAccountDTO.class);

        assertAll(
                () -> assertThat(fetchedBankAccountDTO.getBankAccountId()).isNotNull(),
                () -> assertThat(fetchedBankAccountDTO.getIban()).isNotBlank(),
                () -> assertThat(fetchedBankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC)),
                () -> assertThat(fetchedBankAccountDTO.getAccountHolders())
                        .hasSize(3)
                        .anySatisfy(accountHolderDTO -> {
                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName());
                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber());
                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth());
                        })
                        .anySatisfy(accountHolderDTO -> {
                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(patrizioAccountHolderRequest.accountHolderName());
                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(patrizioAccountHolderRequest.passportNumber());
                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(patrizioAccountHolderRequest.dateOfBirth());
                        })
                        .anySatisfy(accountHolderDTO -> {
                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(virginioAccountHolderRequest.accountHolderName());
                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(virginioAccountHolderRequest.passportNumber());
                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(virginioAccountHolderRequest.dateOfBirth());
                        })
        );
    }

    @Test
    void shouldReturn200Ok_whenBankAccountWithMultipleAccountHoldersExists() {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createdBankAccountDTO = createBankAccountService.create(accountHolderRequest);

        var fetchedBankAccountDTO = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", createdBankAccountDTO.getBankAccountId())
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.getCode())
                .extract()
                    .response()
                        .as(BankAccountDTO.class);

        assertThat(createdBankAccountDTO)
                .usingRecursiveComparison()
                .isEqualTo(fetchedBankAccountDTO);
    }

    @Test
    void shouldReturn404NotFound_whenBankAccountDoesNotExist() {
        var nonExistentBankAccountId = UUID.fromString("08d8cf86-bc25-4535-8b88-920c07d3e5fe");
        var expectedExceptionMessageKey = "bankAccount.notFound";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", nonExistentBankAccountId)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault(), nonExistentBankAccountId)
                        .orElseThrow(() -> new IllegalArgumentException("Message not found for key: " + expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenBankAccountIdIsInvalidUUID() {
        String invalidUUID = "invalid-uuid-format";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", invalidUUID)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
                .rootPath("_embedded")
                    .body("errors", hasSize(1))
                    .body("errors[0].message", containsString("Invalid UUID string: " + invalidUUID));
    }
}
