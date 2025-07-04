package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.bankaccount.enums.AccountStatus;
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
class AccountStatusValueTest {

    @ParameterizedTest
    @EnumSource(AccountStatus.class)
    void shouldCreateAccountStatusValue_whenUsingOfMethod(AccountStatus accountStatus) {
        var result = AccountStatusValue.of(accountStatus);

        assertThat(result)
            .isNotNull()
            .extracting(AccountStatusValue::value)
            .isEqualTo(accountStatus);
    }

    @Test
    void shouldCreatePendingStatus_whenUsingStaticConstructor() {
        assertThat(AccountStatusValue.pending())
            .isEqualTo(AccountStatusValue.of(AccountStatus.PENDING));
    }

    @Test
    void shouldCreateCancelledStatus_whenUsingStaticConstructor() {
        assertThat(AccountStatusValue.cancelled())
            .isEqualTo(AccountStatusValue.of(AccountStatus.CANCELLED));
    }

    @Test
    void shouldCreateActiveStatus_whenUsingStaticConstructor() {
        assertThat(AccountStatusValue.active())
            .isEqualTo(AccountStatusValue.of(AccountStatus.ACTIVE));
    }

    @ParameterizedTest
    @CsvSource({
        "CANCELLED, CANCELLED, true",
        "CANCELLED, PENDING, false",
        "ACTIVE, CANCELLED, false"
    })
    void shouldEvaluateCorrectly_whenCallingIsMethod(AccountStatus source, AccountStatus comparison, boolean expected) {
        var statusValue = AccountStatusValue.of(source);
        assertThat(statusValue.is(comparison)).isEqualTo(expected);
    }

    @Test
    void shouldThrowNullPointerException_whenStatusIsNull() {
        assertThatThrownBy(() -> AccountStatusValue.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.STATUS_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountStatus.class)
    void shouldReturnEnumName_whenCallingToString(AccountStatus status) {
        var statusValue = AccountStatusValue.of(status);
        assertThat(statusValue.toString()).isEqualTo(status.name());
    }
}
