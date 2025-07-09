package com.jcondotta.domain.accountholder.model;

import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.accountholder.enums.AccountHolderType;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    private static final AccountHolderName ACCOUNT_HOLDER_NAME_VO = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);
    private static final PassportNumber PASSPORT_NUMBER_VO = PassportNumber.of(PASSPORT_NUMBER_JEFFERSON);
    private static final DateOfBirth DATE_OF_BIRTH_VO = DateOfBirth.of(DATE_OF_BIRTH_JEFFERSON);

    @Test
    void shouldCreatePrimaryAccountHolder() {
        var primaryAccountHolder = AccountHolder.createPrimary(ACCOUNT_HOLDER_NAME_VO, PASSPORT_NUMBER_VO, DATE_OF_BIRTH_VO, FIXED_CLOCK);

        assertThat(primaryAccountHolder)
            .isNotNull()
            .satisfies(accountHolder -> {
                assertThat(accountHolder.accountHolderId()).isNotNull();
                assertThat(accountHolder.accountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME_VO);
                assertThat(accountHolder.passportNumber()).isEqualTo(PASSPORT_NUMBER_VO);
                assertThat(accountHolder.dateOfBirth()).isEqualTo(DATE_OF_BIRTH_VO);
                assertThat(accountHolder.accountHolderType().isPrimary()).isTrue();
                assertThat(accountHolder.createdAt().value()).isEqualTo(Instant.now(FIXED_CLOCK));
                assertThat(accountHolder.createdAt().zoneId()).isEqualTo(FIXED_CLOCK.getZone());
                assertThat(accountHolder.isPrimary()).isTrue();
                assertThat(accountHolder.isJoint()).isFalse();
            }
        );
    }

    @Test
    void shouldCreateJointAccountHolder() {
        var jointAccountHolder = AccountHolder.createJoint(ACCOUNT_HOLDER_NAME_VO, PASSPORT_NUMBER_VO, DATE_OF_BIRTH_VO, FIXED_CLOCK);

        assertThat(jointAccountHolder)
            .isNotNull()
            .satisfies(accountHolder -> {
                assertThat(accountHolder).isNotNull();
                assertThat(accountHolder.accountHolderId()).isNotNull();
                assertThat(accountHolder.accountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME_VO);
                assertThat(accountHolder.passportNumber()).isEqualTo(PASSPORT_NUMBER_VO);
                assertThat(accountHolder.dateOfBirth()).isEqualTo(DATE_OF_BIRTH_VO);
                assertThat(accountHolder.accountHolderType().isJoint()).isTrue();
                assertThat(accountHolder.createdAt().value()).isEqualTo(Instant.now(FIXED_CLOCK));
                assertThat(accountHolder.createdAt().zoneId()).isEqualTo(FIXED_CLOCK.getZone());
                assertThat(accountHolder.isPrimary()).isFalse();
                assertThat(accountHolder.isJoint()).isTrue();
            }
        );
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldThrowNullPointerException_whenAccountHolderIdIsNull(AccountHolderType accountHolderType) {
        assertThatThrownBy(() -> new AccountHolder(
            null,
            ACCOUNT_HOLDER_NAME_VO,
            PASSPORT_NUMBER_VO,
            DATE_OF_BIRTH_VO,
            accountHolderType,
            CreatedAt.now(FIXED_CLOCK))
        )
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ID_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldThrowNullPointerException_whenAccountHolderNameIsNull(AccountHolderType accountHolderType) {
        assertThatThrownBy(() -> new AccountHolder(
            AccountHolderId.newId(),
            null,
            PASSPORT_NUMBER_VO,
            DATE_OF_BIRTH_VO,
            accountHolderType,
            CreatedAt.now(FIXED_CLOCK))
        )
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.NAME_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldThrowNullPointerException_whenPassportNumberIsNull(AccountHolderType accountHolderType) {
        assertThatThrownBy(() -> new AccountHolder(
            AccountHolderId.newId(),
            ACCOUNT_HOLDER_NAME_VO,
            null,
            DATE_OF_BIRTH_VO,
            accountHolderType,
            CreatedAt.now(FIXED_CLOCK))
        )
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.PASSPORT_NUMBER_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldThrowNullPointerException_whenDateOfBirthIsNull(AccountHolderType accountHolderType) {
        assertThatThrownBy(() -> new AccountHolder(
            AccountHolderId.newId(),
            ACCOUNT_HOLDER_NAME_VO,
            PASSPORT_NUMBER_VO,
            null,
            accountHolderType,
            CreatedAt.now(FIXED_CLOCK))
        )
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.DATE_OF_BIRTH_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderTypeIsNull() {
        assertThatThrownBy(() -> new AccountHolder(
            AccountHolderId.newId(),
            ACCOUNT_HOLDER_NAME_VO,
            PASSPORT_NUMBER_VO,
            DATE_OF_BIRTH_VO,
            null,
            CreatedAt.now(FIXED_CLOCK))
        )
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.AccountHolder.ACCOUNT_HOLDER_TYPE_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(AccountHolderType.class)
    void shouldThrowNullPointerException_whenCreatedAtIsNull(AccountHolderType accountHolderType) {
        assertThatThrownBy(() -> new AccountHolder(
            AccountHolderId.newId(),
            ACCOUNT_HOLDER_NAME_VO,
            PASSPORT_NUMBER_VO,
            DATE_OF_BIRTH_VO,
            accountHolderType,
            null)
        )
            .isInstanceOf(NullPointerException.class)
            .hasMessage(ValidationErrors.CREATED_AT_NOT_NULL);
    }
}
