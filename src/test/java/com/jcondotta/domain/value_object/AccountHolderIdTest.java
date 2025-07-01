package com.jcondotta.domain.value_object;

import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderIdTest {

    @Test
    void shouldCreateAccountHolderId_whenAccountHolderIdValueIsValid() {
        var uuidAccountHolderId = UUID.randomUUID();

        assertThat(AccountHolderId.of(uuidAccountHolderId))
            .isNotNull()
            .isInstanceOf(AccountHolderId.class)
            .extracting(AccountHolderId::value)
            .isEqualTo(uuidAccountHolderId);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderIdValueIsNull() {
        assertThatThrownBy(() -> AccountHolderId.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ID_NOT_NULL);
    }

    @Test
    void shouldBeEqual_whenAccountHolderIdsHaveSameValue() {
        var uuidAccountHolderId = UUID.randomUUID();

        var accountHolderId1 = AccountHolderId.of(uuidAccountHolderId);
        var accountHolderId2 = AccountHolderId.of(uuidAccountHolderId);

        assertThat(accountHolderId1)
            .isEqualTo(accountHolderId2)
            .hasSameHashCodeAs(accountHolderId2);
    }

    @Test
    void shouldNotBeEqual_whenAccountHolderIdsHaveDifferentValues() {
        var accountHolderId1 = AccountHolderId.of(UUID.randomUUID());
        var accountHolderId2 = AccountHolderId.of(UUID.randomUUID());

        assertThat(accountHolderId1)
            .isNotEqualTo(accountHolderId2);
    }
}