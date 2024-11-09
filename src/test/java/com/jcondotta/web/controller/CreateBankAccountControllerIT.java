package com.jcondotta.web.controller;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.domain.BankAccount;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateBankAccountRequest;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.http.HttpStatus;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class CreateBankAccountControllerIT implements LocalStackTestContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankAccountControllerIT.class);

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
    DynamoDbTable<BankAccount> dynamoDbTable;

    @BeforeEach
    void beforeEach(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification
                .basePath(BankAccountURIBuilder.BASE_PATH_API_V1_MAPPING)
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn201Created_whenRequestIsValid() throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var response = given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.getCode())
                .extract()
                    .response();

        var bankAccountDTO = response.as(BankAccountDTO.class);

        var expectedLocation = BankAccountURIBuilder.bankAccountURI(bankAccountDTO.getBankAccountId());
        assertThat(response.header("location")).isEqualTo(expectedLocation.getRawPath());

        assertAll(
                () -> assertThat(bankAccountDTO.getBankAccountId()).isNotNull(),
                () -> assertThat(bankAccountDTO.getAccountHolder().getAccountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME_JEFFERSON),
                () -> assertThat(bankAccountDTO.getAccountHolder().getDateOfBirth()).isEqualTo(DATE_OF_BIRTH_JEFFERSON),
                () -> assertThat(bankAccountDTO.getAccountHolder().getPassportNumber()).isEqualTo(PASSPORT_NUMBER_JEFFERSON),
                () -> assertThat(bankAccountDTO.getIban()).isNotBlank(),
                () -> assertThat(bankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC))
        );

        var bankAccount = dynamoDbTable.getItem(Key.builder()
                .partitionValue(bankAccountDTO.getBankAccountId().toString())
                .build());

        assertThat(bankAccountDTO)
                .usingRecursiveComparison()
                .isEqualTo(new BankAccountDTO(bankAccount));
    }

    @Test
    void shouldReturn400BadRequest_whenRequestHasNullAccountHolder() throws IOException {
        var addBankAccountRequest = new CreateBankAccountRequest(null);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws IOException {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldReturn400BadRequest_whenAccountHolderNameIsLongerThan255Characters() throws IOException {
        final var veryLongAccountHolderName = "J".repeat(256);
        var accountHolderRequest = new AccountHolderRequest(veryLongAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsNull() throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, null, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsInFuture() throws IOException {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, futureDate, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldReturn400BadRequest_whenDateOfBirthIsToday() throws IOException {
        LocalDate today = LocalDate.now();
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, today, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldReturn400BadRequest_whenPassportNumberIsNull() throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, null);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
    void shouldReturn400BadRequest_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) throws IOException {
        var accountHolderRequest = new AccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, invalidLengthPassportNumber);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }
}
