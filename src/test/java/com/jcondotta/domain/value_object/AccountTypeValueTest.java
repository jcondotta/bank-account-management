package com.jcondotta.domain.value_object;

import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.bankaccount.valueobjects.AccountTypeValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountTypeValueTest {

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldCreateAccountTypeValue_whenValueIsValid(AccountType accountType) {
        assertThat(AccountTypeValue.of(accountType))
            .isNotNull()
            .isInstanceOf(AccountTypeValue.class)
            .extracting(AccountTypeValue::value)
            .isEqualTo(accountType);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountTypeValueIsNull() {
        assertThatThrownBy(() -> AccountTypeValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.ACCOUNT_TYPE_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldBeEqual_whenAccountTypeValuesHaveSameValue(AccountType accountType) {
        var value1 = AccountTypeValue.of(accountType);
        var value2 = AccountTypeValue.of(accountType);

        assertThat(value1)
            .isEqualTo(value2)
            .hasSameHashCodeAs(value2);
    }

    @Test
    void shouldNotBeEqual_whenAccountTypeValuesHaveDifferentValues() {
        var value1 = AccountTypeValue.of(AccountType.SAVINGS);
        var value2 = AccountTypeValue.of(AccountType.CHECKING);

        assertThat(value1)
            .isNotEqualTo(value2);
    }
}
