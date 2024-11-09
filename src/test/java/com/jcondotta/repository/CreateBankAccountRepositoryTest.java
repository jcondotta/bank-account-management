package com.jcondotta.repository;

import com.jcondotta.domain.BankAccount;
import com.jcondotta.factory.BankAccountTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccount;
import com.jcondotta.service.dto.BankAccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountRepositoryTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccount.BRAZIL.getBankAccountId();

    @InjectMocks
    private CreateBankAccountRepository createBankAccountRepository;

    @Mock
    private DynamoDbTable<BankAccount> dynamoDbTable;

    @Test
    void shouldSaveBankAccount_whenRecipientIsValid() {
        var bankAccount = BankAccountTestFactory.create(BANK_ACCOUNT_ID_BRAZIL, TestAccountHolderRequest.JEFFERSON);

        var createBankAccountResponse = createBankAccountRepository.create(() -> bankAccount);
        assertThat(createBankAccountResponse.bankAccountDTO())
                .usingRecursiveComparison()
                .isEqualTo(new BankAccountDTO(bankAccount));

        assertThat(createBankAccountResponse.isIdempotent()).isFalse();

        verify(dynamoDbTable).putItem(bankAccount);
        verifyNoMoreInteractions(dynamoDbTable);
    }
}
