package com.jcondotta.domain.value_object;

import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountStatusValueTest {

    @ParameterizedTest
    @EnumSource(AccountStatus.class)
    void shouldCreateBankAccountStatusValue_whenValueIsValid(AccountStatus status) {
        assertThat(AccountStatusValue.of(status))
            .isNotNull()
            .isInstanceOf(AccountStatusValue.class)
            .extracting(AccountStatusValue::value)
            .isEqualTo(status);
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountStatusValueIsNull() {
        assertThatThrownBy(() -> AccountStatusValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.STATUS_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountStatus.class)
    void shouldBeEqual_whenBankAccountStatusValuesHaveSameValue(AccountStatus status) {
        var value1 = AccountStatusValue.of(status);
        var value2 = AccountStatusValue.of(status);

        assertThat(value1)
            .isEqualTo(value2)
            .hasSameHashCodeAs(value2);
    }

    @Test
    void shouldNotBeEqual_whenBankAccountStatusValuesHaveDifferentValues() {
        var value1 = AccountStatusValue.of(AccountStatus.ACTIVE);
        var value2 = AccountStatusValue.of(AccountStatus.CANCELLED);

        assertThat(value1)
            .isNotEqualTo(value2);
    }
}
