package com.jcondotta.domain.value_object;

import com.jcondotta.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.domain.shared.ValidationErrors;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountHolderNameTest {

    @Test
    void shouldCreateAccountHolderName_whenValueIsValid() {
        var accountHolderName = "Jefferson Condotta";

        assertThat(AccountHolderName.of(accountHolderName))
            .isNotNull()
            .isInstanceOf(AccountHolderName.class)
            .extracting(AccountHolderName::value)
            .isEqualTo(accountHolderName);
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldThrowIllegalArgumentException_whenNameIsBlank(String blankValue) {
        assertThatThrownBy(() -> AccountHolderName.of(blankValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ValidationErrors.AccountHolder.NAME_BLANK);
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
        var name = "Jefferson";

        var accountHolderName1 = AccountHolderName.of(name);
        var accountHolderName2 = AccountHolderName.of(name);

        assertThat(accountHolderName1)
            .isEqualTo(accountHolderName2)
            .hasSameHashCodeAs(accountHolderName2);
    }

    @Test
    void shouldNotBeEqual_whenNamesHaveDifferentValues() {
        var accountHolderName1 = AccountHolderName.of("Jefferson");
        var accountHolderName2 = AccountHolderName.of("Condotta");

        assertThat(accountHolderName1)
            .isNotEqualTo(accountHolderName2);
    }
}
