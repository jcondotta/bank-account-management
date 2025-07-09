package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.jcondotta.domain.shared.ValidationErrors.AccountHolder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateAccountHolderDataTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final AccountHolderName ACCOUNT_HOLDER_NAME_VO = AccountHolderName.of(ACCOUNT_HOLDER_NAME);
    private static final PassportNumber PASSPORT_NUMBER_VO = PassportNumber.of(PASSPORT_NUMBER);
    private static final DateOfBirth DATE_OF_BIRTH_VO = DateOfBirth.of(DATE_OF_BIRTH);

    @Test
    void shouldCreateData_whenAllParametersAreValid() {
        var accountHolderData = new CreateAccountHolderData(ACCOUNT_HOLDER_NAME_VO, DATE_OF_BIRTH_VO, PASSPORT_NUMBER_VO);

        assertThat(accountHolderData)
            .isNotNull()
            .satisfies(data -> {
                assertThat(data.accountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME_VO);
                assertThat(data.passportNumber()).isEqualTo(PASSPORT_NUMBER_VO);
                assertThat(data.dateOfBirth()).isEqualTo(DATE_OF_BIRTH_VO);
            });
    }

    @Test
    void shouldCreateData_whenUsingStaticFactoryMethod() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);

        assertThat(accountHolderData)
            .isNotNull()
            .satisfies(data -> {
                assertThat(data.accountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME_VO);
                assertThat(data.passportNumber()).isEqualTo(PASSPORT_NUMBER_VO);
                assertThat(data.dateOfBirth()).isEqualTo(DATE_OF_BIRTH_VO);
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
            Arguments.of(null, DATE_OF_BIRTH_VO, PASSPORT_NUMBER_VO, AccountHolder.NAME_NOT_NULL, "accountHolderName"),
            Arguments.of(ACCOUNT_HOLDER_NAME_VO, null, PASSPORT_NUMBER_VO, AccountHolder.DATE_OF_BIRTH_NOT_NULL, "dateOfBirth"),
            Arguments.of(ACCOUNT_HOLDER_NAME_VO, DATE_OF_BIRTH_VO, null, AccountHolder.PASSPORT_NUMBER_NOT_NULL, "passportNumber")
        );
    }
}