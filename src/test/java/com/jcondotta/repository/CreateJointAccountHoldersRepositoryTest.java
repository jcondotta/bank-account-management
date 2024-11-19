package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.TestAccountHolderFactory;
import com.jcondotta.factory.TestBankAccountFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHoldersRepositoryTest {

    @InjectMocks
    private CreateJointAccountHolderRepository createJointAccountHolderRepository;

    @Mock
    private DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Test
    void shouldSaveAccountHolder_whenRequestHasOneValidAccountHolder() {
        var bankAccount = TestBankAccountFactory.create();
        var jeffersonAccountHolder = TestAccountHolderFactory.createJointAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );

        createJointAccountHolderRepository.create(jeffersonAccountHolder);

        verify(bankingEntityDynamoDbTable).putItem(jeffersonAccountHolder);
        verifyNoMoreInteractions(bankingEntityDynamoDbTable);
    }

    @Test
    void shouldSaveAccountHolder_whenRequestHasMultipleValidAccountHolders() {
        var bankAccount = TestBankAccountFactory.create();
        var jeffersonAccountHolder = TestAccountHolderFactory.createJointAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );
        var patriioAccountHolder = TestAccountHolderFactory.createJointAccountHolder(
                TestAccountHolderRequest.PATRIZIO,
                bankAccount.getBankAccountId()
        );

        var accountHolderRequests = List.of(jeffersonAccountHolder, patriioAccountHolder);

        createJointAccountHolderRepository.create(accountHolderRequests);

        verify(bankingEntityDynamoDbTable).putItem(jeffersonAccountHolder);
        verify(bankingEntityDynamoDbTable).putItem(patriioAccountHolder);
        verifyNoMoreInteractions(bankingEntityDynamoDbTable);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
        var exception = assertThrows(NullPointerException.class, () -> createJointAccountHolderRepository.create((List<BankingEntity>) null));
        assertThat(exception.getMessage()).isEqualTo("accountHolders.notNull");

        verifyNoInteractions(bankingEntityDynamoDbTable);
    }
}