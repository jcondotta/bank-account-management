package com.jcondotta.repository;

import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHolderRepositoryTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    @InjectMocks
    private CreateJointAccountHolderRepository createJointAccountHolderRepository;

    @Mock
    private DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Test
    void shouldCreateAccountHolder_whenAccountHolderIsValid() {
        var jeffersonAccountHolder = TestAccountHolderFactory
                .createJointAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);

        createJointAccountHolderRepository.create(jeffersonAccountHolder);

        verify(bankingEntityDynamoDbTable).putItem(jeffersonAccountHolder);
        verifyNoMoreInteractions(bankingEntityDynamoDbTable);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
        var exception = assertThrows(NullPointerException.class, () -> createJointAccountHolderRepository.create(null));
        assertThat(exception.getMessage()).isEqualTo("accountHolder.notNull");

        verifyNoInteractions(bankingEntityDynamoDbTable);
    }
}