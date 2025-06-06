package com.jcondotta.web.controller;

import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.bank_account.CreateJointAccountHolderService;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import com.jcondotta.service.request.CreateJointAccountHolderRequest;
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
import java.time.LocalDate;
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
class FindBankAccountControllerIT {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    @Autowired
    Clock testClockUTC;

    @Autowired
    CreateBankAccountService createBankAccountService;

    @Autowired
    CreateJointAccountHolderService createJointAccountHolderService;

    @Autowired
    @Qualifier("errorMessageSource")
    MessageSource messageSource;

    @Autowired
    BankAccountURIConfiguration bankAccountURIConfig;

    RequestSpecification requestSpecification;

    @BeforeEach
    void beforeEach(@LocalServerPort int port) {
        requestSpecification = given()
                .baseUri("http://localhost:" + port)
                .basePath(bankAccountURIConfig.bankAccountPath())
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn200Ok_whenBankAccountWithMultipleAccountHoldersExists() {
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
            .statusCode(HttpStatus.OK.value())
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
    void shouldReturn200Ok_whenBankAccountWithAccountHolderExists() {
        var accountHolderRequest = new CreateAccountHolderRequest(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var createdBankAccountDTO = createBankAccountService.create(accountHolderRequest);

        var fetchedBankAccountDTO = given()
            .spec(requestSpecification)
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

    @Test
    void shouldReturn404NotFound_whenBankAccountDoesNotExist() {
        var nonExistentBankAccountId = UUID.fromString("08d8cf86-bc25-4535-8b88-920c07d3e5fe");
        var expectedMessage = messageSource.getMessage("bankAccount.notFound", new Object[]{nonExistentBankAccountId}, Locale.getDefault());

        given()
            .spec(requestSpecification)
                .pathParam("bank-account-id", nonExistentBankAccountId)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("detail", equalTo(expectedMessage));
    }
}
