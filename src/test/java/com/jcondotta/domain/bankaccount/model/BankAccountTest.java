package com.jcondotta.domain.bankaccount.model;

import com.jcondotta.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.accountholder.exceptions.AccountHolderNotFoundException;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.domain.bankaccount.exceptions.PrimaryAccountHolderRemovalNotAllowedException;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.DomainErrorMessages;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class BankAccountTest {

    private static final String ACCOUNT_HOLDER_NAME = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    private static final AccountHolderName ACCOUNT_HOLDER_NAME_VO = AccountHolderName.of(ACCOUNT_HOLDER_NAME);
    private static final PassportNumber PASSPORT_NUMBER_VO = PassportNumber.of(PASSPORT_NUMBER);
    private static final DateOfBirth DATE_OF_BIRTH_VO = DateOfBirth.of(DATE_OF_BIRTH);
    private static final Iban IBAN_VO = Iban.generate();

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldOpenBankAccountWithPrimaryHolder(AccountType accountType, Currency currency) {
        var primaryAccountHolder = AccountHolder.createPrimary(ACCOUNT_HOLDER_NAME_VO, PASSPORT_NUMBER_VO, DATE_OF_BIRTH_VO, FIXED_CLOCK);
        var bankAccount = BankAccount.openBankAccount(accountType, currency, IBAN_VO, primaryAccountHolder, FIXED_CLOCK);

        assertThat(bankAccount)
            .isNotNull()
            .satisfies(account -> {
                assertThat(account.bankAccountId()).isNotNull();
                assertThat(account.accountType()).isEqualTo(accountType);
                assertThat(account.currency()).isEqualTo(currency);
                assertThat(account.iban()).isEqualTo(IBAN_VO);
                assertThat(account.status()).isEqualTo(BankAccount.STATUS_ON_OPENING);
                assertThat(account.createdAt().value()).isEqualTo(CreatedAt.now(FIXED_CLOCK).value());
                assertThat(account.createdAt().zoneId()).isEqualTo(FIXED_CLOCK.getZone());
                assertThat(account.accountHolders()).containsExactly(primaryAccountHolder);
            });
    }

    @Test
    void shouldReturnAccountHolder_whenAccountHolderIsFound() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME);
        var primaryAccountHolder = bankAccount.getPrimaryAccountHolder();

        assertThat(bankAccount.findAccountHolder(primaryAccountHolder.accountHolderId()))
            .isNotEmpty()
            .hasValueSatisfying(accountHolder ->
                assertThat(accountHolder).isEqualTo(primaryAccountHolder));
    }

    @Test
    void shouldReturnEmpty_whenAccountHolderIsNotFound() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME);
        assertThat(bankAccount.findAccountHolder(AccountHolderId.newId())).isEmpty();
    }

    @Test
    void shouldReturnEmpty_whenAccountHolderIdIsNull() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME);
        assertThat(bankAccount.findAccountHolder(null)).isEmpty();
    }

    @Test
    void shouldRemoveAccountHolder_whenAccountHolderIsPresent() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME);

        var jointAccountHolder = TestAccountHolderFactory.createJointWithName("Joint Account Holder #1");
        bankAccount.addJointAccountHolder(jointAccountHolder);

        bankAccount.removeJointAccountHolder(jointAccountHolder.accountHolderId());
        assertThat(bankAccount.jointAccountHolders()).isEmpty();
    }

    @Test
    void shouldThrowAccountHolderNotFoundException_whenAccountHolderIsNotPresent() {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(ACCOUNT_HOLDER_NAME);

        var jointAccountHolder = TestAccountHolderFactory.createJointWithName("Joint Account Holder #1");
        bankAccount.addJointAccountHolder(jointAccountHolder);

        assertThatThrownBy(() -> bankAccount.removeJointAccountHolder(AccountHolderId.newId()))
            .isInstanceOf(AccountHolderNotFoundException.class);

        assertThat(bankAccount.jointAccountHolders())
            .isNotEmpty()
            .hasSize(1)
            .containsExactly(jointAccountHolder);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowPrimaryAccountHolderRemovalNotAllowedException_whenAccountHolderIsPrimary(AccountType accountType, Currency currency) {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(accountType, currency, ACCOUNT_HOLDER_NAME);
        var primaryAccountHolder = bankAccount.getPrimaryAccountHolder();

        assertThatThrownBy(() -> bankAccount.removeJointAccountHolder(primaryAccountHolder.accountHolderId()))
            .isInstanceOf(PrimaryAccountHolderRemovalNotAllowedException.class)
            .satisfies(throwable -> {
                var exception = (PrimaryAccountHolderRemovalNotAllowedException) throwable;
                assertThat(exception.getMessage()).isEqualTo("Only joint account holders can be added");
            });
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void givenPrimaryAccount_shouldThrowException_whenAddJointAccountHolder(AccountType accountType, Currency currency) {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(accountType, currency, ACCOUNT_HOLDER_NAME);
        var extraPrimaryAccountHolder = TestAccountHolderFactory.createPrimaryWithName("Primary Account Holder #2");

        assertThatThrownBy(() -> bankAccount.addJointAccountHolder(extraPrimaryAccountHolder))
            .isInstanceOf(IllegalArgumentException.class)
            .satisfies(throwable -> {
                var exception = (IllegalArgumentException) throwable;
                assertThat(exception.getMessage()).isEqualTo("Only joint account holders can be added");
            });
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowMaxJointAccountHoldersExceededException_whenJointAccountHoldersLimitIsReached(AccountType accountType, Currency currency) {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(accountType, currency, ACCOUNT_HOLDER_NAME);

        var jointAccountHolder1 = TestAccountHolderFactory.createJointWithName("Joint Account Holder #1");
        var jointAccountHolder2 = TestAccountHolderFactory.createJointWithName("Joint Account Holder #2");
        var jointAccountHolder3 = TestAccountHolderFactory.createJointWithName("Joint Account Holder #3");

        Stream.of(jointAccountHolder1, jointAccountHolder2, jointAccountHolder3)
            .forEach(bankAccount::addJointAccountHolder);

        var extraJoint = TestAccountHolderFactory.createJointWithName("Joint Account Holder #4");

        assertThatThrownBy(() -> bankAccount.addJointAccountHolder(extraJoint))
            .isInstanceOf(MaxJointAccountHoldersExceededException.class)
            .satisfies(throwable -> {
                var exception = (MaxJointAccountHoldersExceededException) throwable;
                assertThat(exception.getLimit()).isEqualTo(BankAccount.MAX_JOINT_ACCOUNT_HOLDERS);
                assertThat(exception.getCurrent()).isEqualTo(bankAccount.jointAccountHolders().size());
                assertThat(exception.getMessage()).isEqualTo(DomainErrorMessages.MAX_ACCOUNT_HOLDERS_LIMIT_REACHED);
            });
    }
}