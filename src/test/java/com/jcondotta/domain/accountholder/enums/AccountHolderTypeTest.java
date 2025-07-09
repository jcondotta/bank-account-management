package com.jcondotta.domain.accountholder.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderTypeTest {

    @Test
    void shouldRecognizePrimaryType() {
        var accountHolderType = AccountHolderType.PRIMARY;

        assertThat(accountHolderType.isPrimary()).isTrue();
        assertThat(accountHolderType.isJoint()).isFalse();
    }

    @Test
    void shouldRecognizeJointType() {
        var accountHolderType = AccountHolderType.JOINT;

        assertThat(accountHolderType.isJoint()).isTrue();
        assertThat(accountHolderType.isPrimary()).isFalse();
    }
}