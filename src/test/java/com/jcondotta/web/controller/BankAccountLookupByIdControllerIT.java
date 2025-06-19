package com.jcondotta.web.controller;

import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;
import com.jcondotta.application.dto.create.CreateJointAccountHolderRequest;
import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.application.ports.input.service.CreateBankAccountService;
import com.jcondotta.application.ports.input.service.CreateJointAccountHolderService;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.domain.model.AccountType;
import com.jcondotta.domain.model.Currency;
import com.jcondotta.helper.TestAccountHolderRequest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@ContextConfiguration(initializers = LocalStackTestContainer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankAccountLookupByIdControllerIT {

    private static final AccountType CHECKING_ACCOUNT_TYPE = AccountType.CHECKING;
    private static final Currency CURRENCY_EURO = Currency.EUR;

//    @Autowired
//    Clock testClockUTC;
//
//    @Autowired
//    CreateBankAccountService createBankAccountService;
//
//    @Autowired
//    CreateJointAccountHolderService createJointAccountHolderService;
//
//    @Autowired
//    @Qualifier("errorMessageSource")
//    MessageSource messageSource;
//
//    @Autowired
//    BankAccountURIConfiguration bankAccountURIConfig;
//
//    RequestSpecification requestSpecification;
//
//    @BeforeEach
//    void beforeEach(@LocalServerPort int port) {
//        requestSpecification = given()
//                .baseUri("http://localhost:" + port)
//                .basePath(bankAccountURIConfig.bankAccountPath())
//                .contentType(ContentType.JSON);
//    }
//
//    @Test
//    void shouldReturn200Ok_whenBankAccountWithMultipleAccountHoldersExists() {
//        var jeffersonAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//
//        CreateBankAccountResponse createBankAccountResponse = createBankAccountService.createBankAccount(CreateBankAccountRequest.builder()
//            .accountType(CHECKING_ACCOUNT_TYPE)
//            .currency(CURRENCY_EURO)
//            .accountHolder(jeffersonAccountHolderRequest)
//            .build());
//
//        var patrizioAccountHolderRequest = new CreateJointAccountHolderRequest(
//            TestAccountHolderRequest.PATRIZIO.toAccountHolderRequest()
//        );
//        createJointAccountHolderService.createJointAccountHolder(createBankAccountResponse.bankAccountId().value(), patrizioAccountHolderRequest);
//
//        var lookupByIdResponse = given()
//            .spec(requestSpecification)
//                .pathParam("bank-account-id", createBankAccountResponse.bankAccountId().value())
//        .when()
//            .get()
//        .then()
//            .statusCode(HttpStatus.OK.value())
//                .extract()
//                    .response()
//                        .as(BankAccountLookupResponse.class);
//
//        assertAll(
//            () -> assertThat(lookupByIdResponse.bankAccountId()).isEqualTo(createBankAccountResponse.bankAccountId()),
//            () -> assertThat(lookupByIdResponse.accountType()).isEqualTo(CHECKING_ACCOUNT_TYPE),
//            () -> assertThat(lookupByIdResponse.currency()).isEqualTo(CURRENCY_EURO),
//            () -> assertThat(lookupByIdResponse.iban()).isNotBlank(),
//            () -> assertThat(lookupByIdResponse.dateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC)),
//            () -> assertThat(lookupByIdResponse.accountHolders())
//                .hasSize(2)
//                .anySatisfy(accountHolder -> {
//                    assertThat(accountHolder.getAccountHolderName()).isEqualTo(jeffersonAccountHolderRequest.accountHolderName());
//                    assertThat(accountHolder.getPassportNumber()).isEqualTo(jeffersonAccountHolderRequest.passportNumber());
//                    assertThat(accountHolder.getDateOfBirth()).isEqualTo(jeffersonAccountHolderRequest.dateOfBirth());
//                })
//                .anySatisfy(accountHolder -> {
//                    assertThat(accountHolder.getAccountHolderName()).isEqualTo(patrizioAccountHolderRequest.accountHolder().accountHolderName());
//                    assertThat(accountHolder.getPassportNumber()).isEqualTo(patrizioAccountHolderRequest.accountHolder().passportNumber());
//                    assertThat(accountHolder.getDateOfBirth()).isEqualTo(patrizioAccountHolderRequest.accountHolder().dateOfBirth());
//                })
//        );
//    }
//
//    @Test
//    void shouldReturn404NotFound_whenBankAccountDoesNotExist() {
//        var nonExistentBankAccountId = UUID.fromString("08d8cf86-bc25-4535-8b88-920c07d3e5fe");
//        var expectedMessage = messageSource.getMessage("bankAccount.notFound",
//            new Object[]{ nonExistentBankAccountId }, Locale.getDefault());
//
//        given()
//            .spec(requestSpecification)
//                .pathParam("bank-account-id", nonExistentBankAccountId)
//        .when()
//            .get()
//        .then()
//            .statusCode(HttpStatus.NOT_FOUND.value())
//            .body("detail", equalTo(expectedMessage));
//    }
}
