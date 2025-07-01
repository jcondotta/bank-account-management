package com.jcondotta.domain.value_object;

import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderTypeValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderTypeValueTest {

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldCreateAccountHolderTypeValue_whenValueIsValid(AccountHolderType accountHolderType) {
        assertThat(AccountHolderTypeValue.of(accountHolderType))
            .isNotNull()
            .isInstanceOf(AccountHolderTypeValue.class)
            .extracting(AccountHolderTypeValue::value)
            .isEqualTo(accountHolderType);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderTypeValueIsNull() {
        assertThatThrownBy(() -> AccountHolderTypeValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ACCOUNT_HOLDER_TYPE_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldBeEqual_whenAccountHolderTypeValuesHaveSameValue(AccountHolderType accountHolderType) {
        var value1 = AccountHolderTypeValue.of(accountHolderType);
        var value2 = AccountHolderTypeValue.of(accountHolderType);

        assertThat(value1)
            .isEqualTo(value2)
            .hasSameHashCodeAs(value2);
    }

    @Test
    void shouldNotBeEqual_whenAccountHolderTypeValuesHaveDifferentValues() {
        var value1 = AccountHolderTypeValue.of(AccountHolderType.JOINT);
        var value2 = AccountHolderTypeValue.of(AccountHolderType.PRIMARY);

        assertThat(value1)
            .isNotEqualTo(value2);
    }
}
