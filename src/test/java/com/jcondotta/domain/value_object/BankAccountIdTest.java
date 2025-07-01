package com.jcondotta.domain.value_object;

import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class BankAccountIdTest {

    @Test
    void shouldCreateBankAccountId_whenBankAccountIdValueIsValid() {
        var uuidBankAccountId = UUID.randomUUID();

        assertThat(BankAccountId.of(uuidBankAccountId))
            .isNotNull()
            .isInstanceOf(BankAccountId.class)
            .extracting(BankAccountId::value)
            .isEqualTo(uuidBankAccountId);
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountIdValueIsNull() {
        assertThatThrownBy(() -> BankAccountId.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.ID_NOT_NULL);
    }

    @Test
    void shouldBeEqual_whenBankAccountIdsHaveSameValue() {
        var uuidBankAccountId = UUID.randomUUID();

        var bankAccountId1 = BankAccountId.of(uuidBankAccountId);
        var bankAccountId2 = BankAccountId.of(uuidBankAccountId);

        assertThat(bankAccountId1)
            .isEqualTo(bankAccountId2)
            .hasSameHashCodeAs(bankAccountId2);
    }

    @Test
    void shouldNotBeEqual_whenBankAccountIdsHaveDifferentValues() {
        var bankAccountId1 = BankAccountId.of(UUID.randomUUID());
        var bankAccountId2 = BankAccountId.of(UUID.randomUUID());

        assertThat(bankAccountId1)
            .isNotEqualTo(bankAccountId2);
    }
}
