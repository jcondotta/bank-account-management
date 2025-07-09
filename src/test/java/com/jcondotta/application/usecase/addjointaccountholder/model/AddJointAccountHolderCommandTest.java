package com.jcondotta.application.usecase.addjointaccountholder.model;

import com.jcondotta.application.usecase.shared.helper.TestCreateAccountHolderData;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddJointAccountHolderCommandTest {

    private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.of(UUID.randomUUID());

    @Test
    void shouldCreateCommand_whenAllParametersAreValid() {
        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();
        var command = new AddJointAccountHolderCommand(BANK_ACCOUNT_ID, accountHolderData);

        assertThat(command)
            .satisfies(cmd -> {
                assertThat(cmd.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);

                var holderData = cmd.createAccountHolderData();
                assertThat(holderData.accountHolderName()).isEqualTo(accountHolderData.accountHolderName());
                assertThat(holderData.dateOfBirth()).isEqualTo(accountHolderData.dateOfBirth());
                assertThat(holderData.passportNumber()).isEqualTo(accountHolderData.passportNumber());
            });
    }

    @Test
    void shouldCreateCommand_whenUsingStaticFactoryMethod() {
        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();
        var command = AddJointAccountHolderCommand.of(BANK_ACCOUNT_ID, accountHolderData);

        assertThat(command)
            .satisfies(cmd -> {
                assertThat(cmd.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);

                var holderData = cmd.createAccountHolderData();
                assertThat(holderData.accountHolderName()).isEqualTo(accountHolderData.accountHolderName());
                assertThat(holderData.dateOfBirth()).isEqualTo(accountHolderData.dateOfBirth());
                assertThat(holderData.passportNumber()).isEqualTo(accountHolderData.passportNumber());
            });
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
        var accountHolderData = TestCreateAccountHolderData.JEFFERSON.toCreateAccountHolderData();

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
