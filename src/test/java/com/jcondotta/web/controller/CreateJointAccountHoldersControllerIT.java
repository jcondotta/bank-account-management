package com.jcondotta.web.controller;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.request.AccountHolderRequest;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.MDC;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
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
    void shouldReturn201Created_whenRequestIsValid() throws IOException {
        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();

        var response = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(jeffersonAccountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.getCode())
                .extract();

        var expectedLocation = BankAccountURIBuilder.bankAccountURI(BANK_ACCOUNT_ID_BRAZIL);
        assertThat(response.header("location")).isEqualTo(expectedLocation.getRawPath());

        var jeffersonAccountHolderDTO = response.as(AccountHolderDTO.class);
        assertThat(jeffersonAccountHolderDTO)
                .satisfies(accountHolderDTO -> assertAll(
                        () -> assertThat(accountHolderDTO.getAccountHolderId()).isNotNull(),
                        () -> assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName()),
                        () -> assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber()),
                        () -> assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth()),
                        () -> assertThat(accountHolderDTO.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL)
                ));
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws IOException {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var expectedExceptionMessageKey = "accountHolder.accountHolderName.notBlank";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenAccountHolderNameIsLongerThan255Characters() throws IOException {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);

        var expectedExceptionMessageKey = "accountHolder.accountHolderName.tooLong";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsNull() throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);

        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.notNull";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsInFuture() throws IOException {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);

        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.past";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsToday() throws IOException {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);

        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.past";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenPassportNumberIsNull() throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);

        var expectedExceptionMessageKey = "accountHolder.passportNumber.notNull";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldReturn400BadRequest_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);

        var expectedExceptionMessageKey = "accountHolder.passportNumber.invalidLength";

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }

    @Test
    void shouldReturn400BadRequest_whenRequestBodyIsEmpty(){
        var expectedMessages = Stream.of("accountHolder.passportNumber.notNull", "accountHolder.accountHolderName.notBlank", "accountHolder.dateOfBirth.notNull")
                .map(key -> messageSource.getMessage(key, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(key)))
                .toArray();

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body("{}")
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(expectedMessages.length))
                .body("errors.message", containsInAnyOrder(expectedMessages));
    }
}
