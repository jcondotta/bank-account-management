package com.jcondotta.domain.bankaccount.valueobjects;

import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class BankAccountIdTest {

    private static final UUID BANK_ACCOUNT_UUID_1 = UUID.fromString("1fcaca1b-92ba-43c1-b45c-bacf92868d31");
    private static final UUID BANK_ACCOUNT_UUID_2 = UUID.fromString("d063f4bd-dd1f-41d0-8f47-0d5b9195bfaa");

    @Test
    void shouldCreateBankAccountId_whenValueIsValid() {
        var bankAccountId = BankAccountId.of(BANK_ACCOUNT_UUID_1);

        assertThat(bankAccountId)
            .isNotNull()
            .extracting(BankAccountId::value)
            .isEqualTo(BANK_ACCOUNT_UUID_1);
    }

    @Test
    void shouldGenerateNewBankAccountId_whenCallingNewId() {
        var bankAccountId = BankAccountId.newId();

        assertThat(bankAccountId)
            .isNotNull()
            .extracting(BankAccountId::value)
            .isNotNull();
    }

    @Test
    void shouldReturnTrue_whenComparingWithSameUUID() {
        var bankAccountId = BankAccountId.of(BANK_ACCOUNT_UUID_1);
        assertThat(bankAccountId.is(BANK_ACCOUNT_UUID_1)).isTrue();
    }


    @Test
    void shouldThrowNullPointerException_whenValueIsNull() {
        assertThatThrownBy(() -> BankAccountId.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.ID_NOT_NULL);
    }

    @Test
    void shouldBeEqual_whenBankAccountIdsHaveSameValue() {
        var bankAccountId1 = BankAccountId.of(BANK_ACCOUNT_UUID_1);
        var bankAccountId2 = BankAccountId.of(BANK_ACCOUNT_UUID_1);

        assertThat(bankAccountId1)
            .isEqualTo(bankAccountId2)
            .hasSameHashCodeAs(bankAccountId2);
    }

    @Test
    void shouldNotBeEqual_whenBankAccountIdsHaveDifferentValues() {
        var bankAccountId1 = BankAccountId.of(BANK_ACCOUNT_UUID_1);
        var bankAccountId2 = BankAccountId.of(BANK_ACCOUNT_UUID_2);

        assertThat(bankAccountId1)
            .isNotEqualTo(bankAccountId2);
    }

    @Test
    void shouldReturnStringRepresentation_whenCallingToString() {
        var bankAccountId = BankAccountId.of(BANK_ACCOUNT_UUID_1);
        assertThat(bankAccountId.toString())
            .isEqualTo(BANK_ACCOUNT_UUID_1.toString());
    }
}
