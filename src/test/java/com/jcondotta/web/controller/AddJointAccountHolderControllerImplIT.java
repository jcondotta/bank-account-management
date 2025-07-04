package com.jcondotta.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.infrastructure.properties.BankAccountURIConfiguration;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.interfaces.rest.addjointaccountholder.model.AddJointAccountHolderRestRequest;
import com.jcondotta.interfaces.rest.shared.CreateAccountHolderRestRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ActiveProfiles("test")
@ContextConfiguration(initializers = LocalStackTestContainer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddJointAccountHolderControllerImplIT {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    @Autowired
    Clock testClockUTC;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("errorMessageSource")
    MessageSource messageSource;

    @Autowired
    BankAccountURIConfiguration bankAccountURIConfig;

    RequestSpecification requestSpecification;

    @BeforeAll
    static void beforeAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void beforeEach(@LocalServerPort int port) {
        requestSpecification = given()
                .baseUri("http://localhost:" + port)
                .basePath(bankAccountURIConfig.accountHoldersPath())
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn201Created_whenRequestIsValid() throws JsonProcessingException {
        var request = new AddJointAccountHolderRestRequest(
            TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest()
        );

        var response = given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
                .extract();

//        var expectedLocation = bankAccountURIConfig.bankAccountURI(BANK_ACCOUNT_ID_BRAZIL);
//        assertThat(response.header("location")).isEqualTo(expectedLocation.getRawPath());
//
//        var createJointAccountHolderResponse = response.as(CreateJointAccountHolderResponse.class);
//        assertThat(createJointAccountHolderResponse)
//                .satisfies(accountHolderDTO -> assertAll(
//                        () -> assertThat(accountHolderDTO.accountHolderId()).isNotNull()
//                ));
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws JsonProcessingException {
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
        );

        var expectedMessage = messageSource.getMessage("accountHolder.accountHolderName.notBlank", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.accountHolderName"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenAccountHolderNameIsLongerThan255Characters() throws JsonProcessingException {
        final var veryLongAccountHolderName = "J".repeat(256);
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
        );
        var expectedMessage = messageSource.getMessage("accountHolder.accountHolderName.tooLong", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.accountHolderName"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsNull() throws JsonProcessingException {
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON)
        );
        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.notNull", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.dateOfBirth"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsInFuture() throws JsonProcessingException {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON)
        );
        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.past", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.dateOfBirth"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsToday() throws JsonProcessingException {
        LocalDate today = LocalDate.now();
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON)
        );
        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.past", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.dateOfBirth"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenPassportNumberIsNull() throws JsonProcessingException {
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null)
        );
        var expectedMessage = messageSource.getMessage("accountHolder.passportNumber.notNull", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.passportNumber"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldReturn400BadRequest_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) throws JsonProcessingException {
        var request = new AddJointAccountHolderRestRequest(
            new CreateAccountHolderRestRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber)
        );
        var expectedMessage = messageSource.getMessage("accountHolder.passportNumber.invalidLength", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", BANK_ACCOUNT_ID_BRAZIL)
                .body(objectMapper.writeValueAsString(request))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolder.passportNumber"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }
}
