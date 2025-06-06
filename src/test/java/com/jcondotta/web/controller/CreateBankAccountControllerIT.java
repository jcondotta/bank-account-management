package com.jcondotta.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
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

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@ContextConfiguration(initializers = LocalStackTestContainer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateBankAccountControllerIT {

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
    BankAccountURIConfiguration bankAccountURIConfiguration;

    RequestSpecification requestSpecification;

    @BeforeAll
    static void beforeAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void beforeEach(@LocalServerPort int port) {
        requestSpecification = given()
                .baseUri("http://localhost:" + port)
                .basePath(bankAccountURIConfiguration.rootPath())
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn201CreatedWithValidLocationHeader_whenRequestIsValid() throws IOException {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();

        var response = given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
                .extract()
                    .response();

        var createdBankAccountDTO = response.as(BankAccountDTO.class);

        var expectedLocation = bankAccountURIConfiguration.bankAccountURI(createdBankAccountDTO.getBankAccountId());
        assertThat(response.header("location")).isEqualTo(expectedLocation.getRawPath());

        var fetchedBankAccountDTO = given()
            .spec(requestSpecification.basePath(expectedLocation.getRawPath()))
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
                .extract()
                    .response()
                        .as(BankAccountDTO.class);

        assertThat(createdBankAccountDTO)
                .usingRecursiveComparison()
                .isEqualTo(fetchedBankAccountDTO);
    }

    @Test
    void shouldReturn201CreatedWithValidBody_whenRequestIsValid() throws IOException {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();

        var createdBankAccountDTO = given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
                .extract()
                    .response()
                        .as(BankAccountDTO.class);

        assertAll(
                () -> assertThat(createdBankAccountDTO.getBankAccountId()).isNotNull(),
                () -> assertThat(createdBankAccountDTO.getIban()).isNotBlank(),
                () -> assertThat(createdBankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC)),
                () -> assertThat(createdBankAccountDTO.getAccountHolders())
                        .hasSize(1)
                        .extracting(AccountHolderDTO::getAccountHolderName, AccountHolderDTO::getDateOfBirth, AccountHolderDTO::getPassportNumber)
                        .containsExactly(
                                tuple(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
                        )
        );

        var fetchedBankAccountDTO = given()
            .spec(requestSpecification.basePath(bankAccountURIConfiguration.bankAccountPath()))
                .pathParam("bank-account-id", createdBankAccountDTO.getBankAccountId())
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
                .extract()
                    .response()
                        .as(BankAccountDTO.class);

        assertThat(createdBankAccountDTO)
                .usingRecursiveComparison()
                .isEqualTo(fetchedBankAccountDTO);
    }

    @ParameterizedTest(name = "Blank value test: {index} - Input: [{0}]")
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws IOException {
        var accountHolderRequest = new CreateAccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var expectedMessage = messageSource.getMessage("accountHolder.accountHolderName.notBlank", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolderName"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenAccountHolderNameIsLongerThan255Characters() throws IOException {
        var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new CreateAccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var expectedMessage = messageSource.getMessage("accountHolder.accountHolderName.tooLong", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("accountHolderName"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsNull() throws IOException {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);
        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.notNull", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("dateOfBirth"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsInFuture() throws IOException {
        var futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.past", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("dateOfBirth"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsToday() throws IOException {
        var todayDate = LocalDate.now();
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, todayDate, PASSPORT_NUMBER_JEFFERSON);
        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.past", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("dateOfBirth"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @Test
    void shouldReturn400BadRequest_whenPassportNumberIsNull() throws IOException {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var expectedMessage = messageSource.getMessage("accountHolder.passportNumber.notNull", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("passportNumber"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldReturn400BadRequest_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) throws IOException {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var expectedMessage = messageSource.getMessage("accountHolder.passportNumber.invalidLength", null, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .body(objectMapper.writeValueAsString(accountHolderRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors", hasSize(1))
            .body("errors[0].field", equalTo("passportNumber"))
            .body("errors[0].messages", hasSize(1))
            .body("errors[0].messages[0]", equalTo(expectedMessage));
    }
}
