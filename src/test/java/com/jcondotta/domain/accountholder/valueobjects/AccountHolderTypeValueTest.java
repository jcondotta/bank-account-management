package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderTypeValueTest {

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldCreateAccountHolderTypeValue_whenUsingOfMethod(AccountHolderType type) {
        var result = AccountHolderTypeValue.of(type);

        assertThat(result)
            .isNotNull()
            .extracting(AccountHolderTypeValue::value)
            .isEqualTo(type);
    }

    @Test
    void shouldCreatePrimaryType_whenUsingStaticConstructor() {
        assertThat(AccountHolderTypeValue.primary())
            .isEqualTo(AccountHolderTypeValue.of(AccountHolderType.PRIMARY));
    }

    @Test
    void shouldCreateJointType_whenUsingStaticConstructor() {
        assertThat(AccountHolderTypeValue.joint())
            .isEqualTo(AccountHolderTypeValue.of(AccountHolderType.JOINT));
    }

    @ParameterizedTest
    @CsvSource({
        "PRIMARY, PRIMARY, true",
        "JOINT, JOINT, true",
        "PRIMARY, JOINT, false",
        "JOINT, PRIMARY, false"
    })
    void shouldEvaluateCorrectly_whenCallingIsMethod(AccountHolderType source, AccountHolderType comparison, boolean expected) {
        var typeValue = AccountHolderTypeValue.of(source);
        assertThat(typeValue.is(comparison)).isEqualTo(expected);
    }

    @Test
    void shouldThrowNullPointerException_whenTypeIsNull() {
        assertThatThrownBy(() -> AccountHolderTypeValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ACCOUNT_HOLDER_TYPE_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldReturnEnumName_whenCallingToString(AccountHolderType type) {
        var value = AccountHolderTypeValue.of(type);
        assertThat(value.toString()).isEqualTo(type.name());
    }
}
