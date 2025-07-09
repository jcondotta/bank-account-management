package com.jcondotta.application.usecase.shared.mapper;

import com.jcondotta.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.bankaccount.enums.AccountType;
import com.jcondotta.domain.bankaccount.enums.Currency;
import com.jcondotta.application.usecase.shared.helper.TestCreateAccountHolderData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Clock;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BankAccountDetailsMapperImplTest {

    private static final String ACCOUNT_HOLDER_NAME = TestCreateAccountHolderData.JEFFERSON.getAccountHolderName().value();
    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    private BankAccountDetailsMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new BankAccountDetailsMapperImpl(new AccountHolderDetailsMapperImpl());
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
    void shouldMapBankAccountDetails_whenBankAccountHasMultipleHolders(AccountType accountType, Currency currency) {
        var bankAccount = TestBankAccountFactory.createWithPrimaryAccountHolder(accountType, currency, ACCOUNT_HOLDER_NAME, FIXED_CLOCK);
        var primaryAccountHolder = bankAccount.getPrimaryAccountHolder();

        var jointAccountHolder1 = TestAccountHolderFactory.createJointWithName("Joint AccountHolder #1", FIXED_CLOCK);
        var jointAccountHolder2 = TestAccountHolderFactory.createJointWithName("Joint AccountHolder #2", FIXED_CLOCK);
        Stream.of(jointAccountHolder1, jointAccountHolder2).forEach(bankAccount::addJointAccountHolder);

        assertThat(mapper.toBankAccountDetails(bankAccount))
            .satisfies(details -> assertAll(
                () -> assertThat(details.bankAccountId()).isEqualTo(bankAccount.bankAccountId()),
                () -> assertThat(details.iban()).isEqualTo(bankAccount.iban()),
                () -> assertThat(details.accountType()).isEqualTo(bankAccount.accountType()),
                () -> assertThat(details.currency()).isEqualTo(bankAccount.currency()),
                () -> assertThat(details.accountStatus()).isEqualTo(bankAccount.status()),
                () -> assertThat(details.dateOfOpening()).isEqualTo(bankAccount.createdAt()),
                () -> assertThat(details.accountHolders())
                    .hasSize(3)
                    .anySatisfy(accountHolder -> {
                        assertThat(accountHolder.accountHolderId()).isEqualTo(primaryAccountHolder.accountHolderId());
                        assertThat(accountHolder.accountHolderName()).isEqualTo(primaryAccountHolder.accountHolderName());
                        assertThat(accountHolder.passportNumber()).isEqualTo(primaryAccountHolder.passportNumber());
                        assertThat(accountHolder.dateOfBirth()).isEqualTo(primaryAccountHolder.dateOfBirth());
                        assertThat(accountHolder.createdAt()).isEqualTo(primaryAccountHolder.createdAt());
                    })
                    .anySatisfy(accountHolder -> {
                        assertThat(accountHolder.accountHolderId()).isEqualTo(jointAccountHolder1.accountHolderId());
                        assertThat(accountHolder.accountHolderName()).isEqualTo(jointAccountHolder1.accountHolderName());
                        assertThat(accountHolder.passportNumber()).isEqualTo(jointAccountHolder1.passportNumber());
                        assertThat(accountHolder.dateOfBirth()).isEqualTo(jointAccountHolder1.dateOfBirth());
                        assertThat(accountHolder.createdAt()).isEqualTo(jointAccountHolder1.createdAt());
                    })
                    .anySatisfy(accountHolder -> {
                        assertThat(accountHolder.accountHolderId()).isEqualTo(jointAccountHolder2.accountHolderId());
                        assertThat(accountHolder.accountHolderName()).isEqualTo(jointAccountHolder2.accountHolderName());
                        assertThat(accountHolder.passportNumber()).isEqualTo(jointAccountHolder2.passportNumber());
                        assertThat(accountHolder.dateOfBirth()).isEqualTo(jointAccountHolder2.dateOfBirth());
                        assertThat(accountHolder.createdAt()).isEqualTo(jointAccountHolder2.createdAt());
                    })
            ));
    }
}
