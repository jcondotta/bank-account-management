package com.jcondotta.domain.bankaccount.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTypeTest {

    @Test
    void shouldRecognizeSavingsType() {
        var accountType = AccountType.SAVINGS;

        assertThat(accountType.isSavings()).isTrue();
        assertThat(accountType.isChecking()).isFalse();
    }

    @Test
    void shouldRecognizeCheckingType() {
        var accountType = AccountType.CHECKING;

        assertThat(accountType.isSavings()).isFalse();
        assertThat(accountType.isChecking()).isTrue();
    }
}