package com.jcondotta.application.usecase.addjointaccountholder.model;

import com.jcondotta.application.usecase.shared.model.CreateAccountHolderData;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddJointAccountHolderCommandTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.of(UUID.randomUUID());

    @Test
    void shouldCreateCommand_whenAllParametersAreValid() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = new AddJointAccountHolderCommand(BANK_ACCOUNT_ID, accountHolderData);

        assertThat(command)
            .satisfies(cmd -> {
                assertThat(cmd.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);

                var holderData = cmd.createAccountHolderData();
                assertThat(holderData.accountHolderName().value()).isEqualTo(ACCOUNT_HOLDER_NAME);
                assertThat(holderData.dateOfBirth().value()).isEqualTo(DATE_OF_BIRTH);
                assertThat(holderData.passportNumber().value()).isEqualTo(PASSPORT_NUMBER);
            });
    }

    @Test
    void shouldCreateCommand_whenUsingStaticFactoryMethod() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);
        var command = AddJointAccountHolderCommand.of(BANK_ACCOUNT_ID, accountHolderData);

        assertThat(command)
            .satisfies(cmd -> {
                assertThat(cmd.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);

                var holderData = cmd.createAccountHolderData();
                assertThat(holderData.accountHolderName().value()).isEqualTo(ACCOUNT_HOLDER_NAME);
                assertThat(holderData.dateOfBirth().value()).isEqualTo(DATE_OF_BIRTH);
                assertThat(holderData.passportNumber().value()).isEqualTo(PASSPORT_NUMBER);
            });
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
        var accountHolderData = CreateAccountHolderData.of(ACCOUNT_HOLDER_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER);

        assertThatThrownBy(() -> new AddJointAccountHolderCommand(null, accountHolderData))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.BankAccount.ID_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderDataIsNull() {
        assertThatThrownBy(() -> new AddJointAccountHolderCommand(BANK_ACCOUNT_ID, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ACCOUNT_HOLDER_DATA_NOT_NULL);
    }
}
