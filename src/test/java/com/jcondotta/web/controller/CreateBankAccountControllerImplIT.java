package com.jcondotta.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.infrastructure.properties.BankAccountURIConfiguration;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@ContextConfiguration(initializers = LocalStackTestContainer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateBankAccountControllerImplIT {

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

//    @ParameterizedTest
//    @ArgumentsSource(BankAccountTypeAndCurrencyArgumentsProvider.class)
//    void shouldReturn201CreatedWithValidLocationHeader_whenRequestIsValid(AccountType accountType, Currency currency) throws IOException {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(accountType)
//            .currency(currency)
//            .accountHolder(TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest())
//            .build();
//
//        var response = given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.CREATED.value())
//                .extract()
//                    .response();
//
//        var createBankAccountResponse = response.as(CreateBankAccountResponse.class);
//
//        var expectedLocation = bankAccountURIConfiguration.bankAccountURI(createBankAccountResponse.bankAccountId().value());
//        assertThat(response.header("location")).isEqualTo(expectedLocation.getRawPath());
//
//        var bankAccountLookupByIdResponse = given()
//            .spec(requestSpecification.basePath(expectedLocation.getRawPath()))
//        .when()
//            .get()
//        .then()
//            .statusCode(HttpStatus.OK.value())
//                .extract()
//                    .response()
//                        .as(BankAccountLookupResponse.class);
//
//        assertAll(
//            () -> assertThat(bankAccountLookupByIdResponse.bankAccountId()).isEqualTo(createBankAccountResponse.bankAccountId())
////            () -> assertThat(bankAccountLookupByIdResponse.iban()).isNotBlank(),
////            () -> assertThat(bankAccountLookupByIdResponse.dateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC)),
////            () -> assertThat(bankAccountLookupByIdResponse.accountHolders())
////                .hasSize(1)
////                .extracting(AccountHolderDetailsResponse::getAccountHolderName, AccountHolderDetailsResponse::getDateOfBirth, AccountHolderDetailsResponse::getPassportNumber)
////                .containsExactly(
////                    tuple(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
////                )
//        );
//    }
//
//    @ParameterizedTest(accountHolderName = "Blank value test: {index} - Input: [{0}]")
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws IOException {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                blankAccountHolderName,
//                DATE_OF_BIRTH_JEFFERSON,
//                PASSPORT_NUMBER_JEFFERSON)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.accountHolderName.notBlank", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.accountHolderName"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenAccountHolderNameIsLongerThan255Characters() throws IOException {
//        var veryLongAccountHolderName = "J".repeat(256);
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                veryLongAccountHolderName,
//                DATE_OF_BIRTH_JEFFERSON,
//                PASSPORT_NUMBER_JEFFERSON)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.accountHolderName.tooLong", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.accountHolderName"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenDateOfBirthIsNull() throws IOException {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                null,
//                PASSPORT_NUMBER_JEFFERSON)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.notNull", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.dateOfBirth"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenDateOfBirthIsInFuture() throws IOException {
//        var futureDate = LocalDate.now().plusDays(1);
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                futureDate,
//                PASSPORT_NUMBER_JEFFERSON)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.past", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.dateOfBirth"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenDateOfBirthIsToday() throws IOException {
//        var todayDate = LocalDate.now();
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                todayDate,
//                PASSPORT_NUMBER_JEFFERSON)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.dateOfBirth.past", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.dateOfBirth"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
//
//    @Test
//    void shouldReturn400BadRequest_whenPassportNumberIsNull() throws IOException {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                DATE_OF_BIRTH_JEFFERSON,
//                null)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.passportNumber.notNull", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.passportNumber"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(InvalidPassportNumberArgumentProvider.class)
//    void shouldReturn400BadRequest_whenPassportNumberIsNot8CharactersLong(String invalidLengthPassportNumber) throws IOException {
//        var request = CreateBankAccountRequest.builder()
//            .accountType(AccountType.CHECKING)
//            .currency(Currency.EUR)
//            .accountHolder(new AccountHolderDetailsRequest(
//                ACCOUNT_HOLDER_NAME_JEFFERSON,
//                DATE_OF_BIRTH_JEFFERSON,
//                invalidLengthPassportNumber)
//            )
//            .build();
//
//        var expectedMessage = messageSource.getMessage("accountHolder.passportNumber.invalidLength", null, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .body(objectMapper.writeValueAsString(request))
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value())
//            .body("errors", hasSize(1))
//            .body("errors[0].field", equalTo("accountHolder.passportNumber"))
//            .body("errors[0].messages", hasSize(1))
//            .body("errors[0].messages[0]", equalTo(expectedMessage));
//    }
}
