package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderIdTest {

    private static final UUID ACCOUNT_HOLDER_UUID_1 = UUID.fromString("5f0bdde3-5d79-4f8c-bdde-6f3c3a00dfc1");
    private static final UUID ACCOUNT_HOLDER_UUID_2 = UUID.fromString("e79f3c32-f74c-4bd6-b601-4d12f13ea71c");

    @Test
    void shouldCreateAccountHolderId_whenValueIsValid() {
        var accountHolderId = AccountHolderId.of(ACCOUNT_HOLDER_UUID_1);

        assertThat(accountHolderId)
            .isNotNull()
            .extracting(AccountHolderId::value)
            .isEqualTo(ACCOUNT_HOLDER_UUID_1);
    }

    @Test
    void shouldGenerateNewAccountHolderId_whenCallingNewId() {
        var accountHolderId = AccountHolderId.newId();

        assertThat(accountHolderId)
            .isNotNull()
            .extracting(AccountHolderId::value)
            .isNotNull();
    }

    @Test
    void shouldReturnTrue_whenComparingWithSameUUID() {
        var accountHolderId = AccountHolderId.of(ACCOUNT_HOLDER_UUID_1);
        assertThat(accountHolderId.is(ACCOUNT_HOLDER_UUID_1)).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenValueIsNull() {
        assertThatThrownBy(() -> AccountHolderId.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ID_NOT_NULL);
    }

    @Test
    void shouldBeEqual_whenAccountHolderIdsHaveSameValue() {
        var accountHolderId1 = AccountHolderId.of(ACCOUNT_HOLDER_UUID_1);
        var accountHolderId2 = AccountHolderId.of(ACCOUNT_HOLDER_UUID_1);

        assertThat(accountHolderId1)
            .isEqualTo(accountHolderId2)
            .hasSameHashCodeAs(accountHolderId2);
    }

    @Test
    void shouldNotBeEqual_whenAccountHolderIdsHaveDifferentValues() {
        var accountHolderId1 = AccountHolderId.of(ACCOUNT_HOLDER_UUID_1);
        var accountHolderId2 = AccountHolderId.of(ACCOUNT_HOLDER_UUID_2);

        assertThat(accountHolderId1).isNotEqualTo(accountHolderId2);
    }

    @Test
    void shouldReturnStringRepresentation_whenCallingToString() {
        var accountHolderId = AccountHolderId.of(ACCOUNT_HOLDER_UUID_1);

        assertThat(accountHolderId.toString())
            .isEqualTo(ACCOUNT_HOLDER_UUID_1.toString());
    }
}
