package com.jcondotta.domain.bankaccount.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStatusTest {

    @Test
    void shouldRecognizePendingStatus() {
        var accountStatus = AccountStatus.PENDING;

        assertThat(accountStatus.isPending()).isTrue();
        assertThat(accountStatus.isActive()).isFalse();
        assertThat(accountStatus.isCancelled()).isFalse();
    }

    @Test
    void shouldRecognizeActiveStatus() {
        var accountStatus = AccountStatus.ACTIVE;

        assertThat(accountStatus.isPending()).isFalse();
        assertThat(accountStatus.isActive()).isTrue();
        assertThat(accountStatus.isCancelled()).isFalse();
    }

    @Test
    void shouldRecognizeCancelledStatus() {
        var accountStatus = AccountStatus.CANCELLED;

        assertThat(accountStatus.isPending()).isFalse();
        assertThat(accountStatus.isActive()).isFalse();
        assertThat(accountStatus.isCancelled()).isTrue();
    }
}