package com.jcondotta.domain.accountholder.valueobjects;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.domain.shared.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderNameTest {

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = "Jefferson Condotta";
    private static final String ACCOUNT_HOLDER_NAME_PATRIZIO = "Patrizio Condotta";

    @Test
    void shouldCreateAccountHolderName_whenValueIsValid() {
        assertThat(AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON))
            .isNotNull()
            .extracting(AccountHolderName::value)
            .isEqualTo(ACCOUNT_HOLDER_NAME_JEFFERSON);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowIllegalArgumentException_whenNameIsBlank(String blankValue) {
        assertThatThrownBy(() -> AccountHolderName.of(blankValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.AccountHolder.NAME_NOT_BLANK);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenNameIsTooLong() {
        var longName = "A".repeat(256);

        assertThatThrownBy(() -> AccountHolderName.of(longName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.AccountHolder.NAME_TOO_LONG);
    }

    @Test
    void shouldBeEqual_whenNamesHaveSameValue() {
        var accountHolderName1 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);
        var accountHolderName2 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);

        assertThat(accountHolderName1)
            .isEqualTo(accountHolderName2)
            .hasSameHashCodeAs(accountHolderName2);
    }

    @Test
    void shouldNotBeEqual_whenNamesHaveDifferentValues() {
        var accountHolderName1 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);
        var accountHolderName2 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_PATRIZIO);

        assertThat(accountHolderName1)
            .isNotEqualTo(accountHolderName2);
    }
}
