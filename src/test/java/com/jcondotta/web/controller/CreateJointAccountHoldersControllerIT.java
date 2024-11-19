package com.jcondotta.web.controller;

import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.service.dto.AccountHoldersDTO;
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
    void shouldReturn201Created_whenRequestAccountHoldersListHasOneItem() throws IOException {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var accountHoldersRequest = List.of(accountHolderRequest);

        var response = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(jsonMapper.writeValueAsString(accountHoldersRequest))
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

//    @Test
//    void shouldReturn201CreatedWithValidBody_whenRequestIsValid() throws IOException {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var createdBankAccountDTO = given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.CREATED.getCode())
//                .extract()
//                    .response()
//                        .as(BankAccountDTO.class);
//
//        assertAll(
//                () -> assertThat(createdBankAccountDTO.getBankAccountId()).isNotNull(),
//                () -> assertThat(createdBankAccountDTO.getIban()).isNotBlank(),
//                () -> assertThat(createdBankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC)),
//                () -> assertThat(createdBankAccountDTO.getAccountHolders())
//                        .hasSize(1)
//                        .extracting(AccountHolderDTO::getAccountHolderName, AccountHolderDTO::getDateOfBirth, AccountHolderDTO::getPassportNumber)
//                        .containsExactly(
//                                tuple(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
//                        )
//        );
//
//        var fetchedBankAccountDTO = given()
//            .spec(requestSpecification.basePath(BankAccountURIBuilder.BANK_ACCOUNT_API_V1_MAPPING))
//                .pathParam("bank-account-id", createdBankAccountDTO.getBankAccountId())
//        .when()
//            .get()
//        .then()
//            .statusCode(HttpStatus.OK.getCode())
//                .extract()
//                    .response()
//                        .as(BankAccountDTO.class);
//
//        assertThat(createdBankAccountDTO)
//                .usingRecursiveComparison()
//                .isEqualTo(fetchedBankAccountDTO);
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenRequestHasNullAccountHolder() throws IOException {
//        var createBankAccountRequest = new CreateBankAccountRequest(null);
//        var expectedExceptionMessageKey = "bankAccount.accountHolder.notNull";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws IOException {
//        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.accountHolderName.notBlank";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenAccountHolderNameIsLongerThan255Characters() throws IOException {
//        final var veryLongAccountHolderName = "J".repeat(256);
//        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.accountHolderName.tooLong";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenDateOfBirthIsNull() throws IOException {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.notNull";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenDateOfBirthIsInFuture() throws IOException {
//        LocalDate futureDate = LocalDate.now().plusDays(1);
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.past";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenDateOfBirthIsToday() throws IOException {
//        LocalDate today = LocalDate.now();
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.past";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenPassportNumberIsNull() throws IOException {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.passportNumber.notNull";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
//    void shouldReturn400BadRequest_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) throws IOException {
//        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
//        var createBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);
//
//        var expectedExceptionMessageKey = "accountHolder.passportNumber.invalidLength";
//
//        given()
//            .spec(requestSpecification)
//                .body(jsonMapper.writeValueAsString(createBankAccountRequest))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
//                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
//    }
}