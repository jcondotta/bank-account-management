package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountTypeValueTest {

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldCreateAccountTypeValue_whenUsingOfMethod(AccountType type) {
        var result = AccountTypeValue.of(type);

        assertThat(result)
            .isNotNull()
            .extracting(AccountTypeValue::value)
            .isEqualTo(type);
    }

    @Test
    void shouldCreateCheckingType_whenUsingStaticConstructor() {
        assertThat(AccountTypeValue.checking())
            .isEqualTo(AccountTypeValue.of(AccountType.CHECKING));
    }

    @Test
    void shouldCreateSavingsType_whenUsingStaticConstructor() {
        assertThat(AccountTypeValue.savings())
            .isEqualTo(AccountTypeValue.of(AccountType.SAVINGS));
    }

    @ParameterizedTest
    @CsvSource({
        "CHECKING, CHECKING, true",
        "SAVINGS, SAVINGS, true",
        "CHECKING, SAVINGS, false",
        "SAVINGS, CHECKING, false"
    })
    void shouldEvaluateCorrectly_whenCallingIsMethod(AccountType source, AccountType comparison, boolean expected) {
        var typeValue = AccountTypeValue.of(source);
        assertThat(typeValue.is(comparison)).isEqualTo(expected);
    }

    @Test
    void shouldThrowNullPointerException_whenTypeIsNull() {
        assertThatThrownBy(() -> AccountTypeValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldReturnEnumName_whenCallingToString(AccountType type) {
        var typeValue = AccountTypeValue.of(type);
        assertThat(typeValue.toString()).isEqualTo(type.name());
    }
}
