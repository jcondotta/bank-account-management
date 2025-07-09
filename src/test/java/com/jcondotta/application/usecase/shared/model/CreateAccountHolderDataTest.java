package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.application.usecase.shared.helper.TestCreateAccountHolderData;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.jcondotta.domain.shared.ValidationErrors.AccountHolder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateAccountHolderDataTest {

    private static final AccountHolderName ACCOUNT_HOLDER_NAME = TestCreateAccountHolderData.JEFFERSON.getAccountHolderName();
    private static final PassportNumber PASSPORT_NUMBER = TestCreateAccountHolderData.JEFFERSON.getPassportNumber();
    private static final DateOfBirth DATE_OF_BIRTH = TestCreateAccountHolderData.JEFFERSON.getDateOfBirth();

    @Test
    void shouldCreateData_whenAllParametersAreValid() {
        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();

        assertThat(accountHolderData)
            .isNotNull()
            .satisfies(holderData -> {
                assertThat(holderData.accountHolderName()).isEqualTo(accountHolderData.accountHolderName());
                assertThat(holderData.dateOfBirth()).isEqualTo(accountHolderData.dateOfBirth());
                assertThat(holderData.passportNumber()).isEqualTo(accountHolderData.passportNumber());
            });
    }

    @Test
    void shouldCreateData_whenUsingStaticFactoryMethod() {
        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();

        assertThat(accountHolderData)
            .isNotNull()
            .satisfies(holderData -> {
                assertThat(holderData.accountHolderName()).isEqualTo(accountHolderData.accountHolderName());
                assertThat(holderData.dateOfBirth()).isEqualTo(accountHolderData.dateOfBirth());
                assertThat(holderData.passportNumber()).isEqualTo(accountHolderData.passportNumber());
            });
    }

    @ParameterizedTest(name = "[{index}] Should throw NullPointerException when {4} is null")
    @MethodSource("nullFieldScenarios")
    void shouldThrowNullPointerException_whenAnyFieldIsNull(
        AccountHolderName accountHolderName,
        DateOfBirth dateOfBirth,
        PassportNumber passportNumber,
        String expectedMessage,
        String fieldName
    ) {
        assertThatThrownBy(() -> new CreateAccountHolderData(accountHolderName, dateOfBirth, passportNumber))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(expectedMessage);
    }

    private static Stream<Arguments> nullFieldScenarios() {
        return Stream.of(
            Arguments.of(null, DATE_OF_BIRTH, PASSPORT_NUMBER, AccountHolder.NAME_NOT_NULL, "accountHolderName"),
            Arguments.of(ACCOUNT_HOLDER_NAME, null, PASSPORT_NUMBER, AccountHolder.DATE_OF_BIRTH_NOT_NULL, "dateOfBirth"),
            Arguments.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, null, AccountHolder.PASSPORT_NUMBER_NOT_NULL, "passportNumber")
        );
    }
}