package com.jcondotta.web.controller;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.argument_provider.InvalidPassportNumberArgumentProvider;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.BankAccount;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.AccountHolderRequest;
import com.jcondotta.service.request.CreateBankAccountRequest;
import com.jcondotta.web.controller.bank_account.BankAccountURIBuilder;
import io.micronaut.context.MessageSource;
import io.micronaut.http.HttpStatus;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class CreateBankAccountControllerIT implements LocalStackTestContainer {

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
    DynamoDbTable<BankAccount> dynamoDbBankAccountTable;

    @Inject
    DynamoDbTable<AccountHolder> dynamoDbAccountHolderTable;

    @Inject
    @Named("exceptionMessageSource")
    MessageSource messageSource;

    public static Supplier<IllegalArgumentException> messageKeyNotFoundException(String messageKey) {
        return () -> new IllegalArgumentException("Message not found for key: " + messageKey);
    }

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
                () -> assertThat(bankAccountDTO.getIban()).isNotBlank(),
                () -> assertThat(bankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(testClockUTC)),
                () -> assertThat(bankAccountDTO.getAccountHolders())
                        .hasSize(1)
                        .extracting(AccountHolderDTO::getAccountHolderName, AccountHolderDTO::getDateOfBirth, AccountHolderDTO::getPassportNumber)
                        .containsExactly(
                                tuple(ACCOUNT_HOLDER_NAME_JEFFERSON, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON)
                        )
        );

        var bankAccount = dynamoDbBankAccountTable.getItem(Key.builder()
                .partitionValue(BankAccount.PARTITION_KEY_TEMPLATE.formatted(bankAccountDTO.getBankAccountId().toString()))
                .sortValue(BankAccount.SORT_KEY_TEMPLATE.formatted(bankAccountDTO.getBankAccountId().toString()))
                .build());

        var accountHolder = dynamoDbAccountHolderTable.getItem(Key.builder()
                .partitionValue(AccountHolder.PARTITION_KEY_TEMPLATE.formatted(bankAccountDTO.getBankAccountId().toString()))
                .sortValue(AccountHolder.SORT_KEY_TEMPLATE.formatted(bankAccountDTO.getAccountHolders().get(0).getAccountHolderId().toString()))
                .build());

        assertThat(bankAccountDTO)
                .usingRecursiveComparison()
                .isEqualTo(new BankAccountDTO(bankAccount, accountHolder));
    }

    @Test
    void shouldReturn400BadRequest_whenRequestHasNullAccountHolder() throws IOException {
        var addBankAccountRequest = new CreateBankAccountRequest(null);
        var expectedExceptionMessageKey = "bankAccount.accountHolder.notNull";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldReturn400BadRequest_whenAccountHolderNameIsBlank(String blankAccountHolderName) throws IOException {
        var accountHolderRequest = new AccountHolderRequest(blankAccountHolderName, DATE_OF_BIRTH_JEFFERSON, PASSPORT_NUMBER_JEFFERSON);
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.accountHolderName.notBlank";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.accountHolderName.tooLong";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.notNull";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.past";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.dateOfBirth.past";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.passportNumber.notNull";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
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
        var addBankAccountRequest = new CreateBankAccountRequest(accountHolderRequest);

        var expectedExceptionMessageKey = "accountHolder.passportNumber.invalidLength";

        given()
            .spec(requestSpecification)
                .body(jsonMapper.writeValueAsString(addBankAccountRequest))
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode())
            .rootPath("_embedded")
                .body("errors", hasSize(1))
                .body("errors[0].message", equalTo(messageSource.getMessage(expectedExceptionMessageKey, Locale.getDefault())
                        .orElseThrow(messageKeyNotFoundException(expectedExceptionMessageKey))));
    }
}
