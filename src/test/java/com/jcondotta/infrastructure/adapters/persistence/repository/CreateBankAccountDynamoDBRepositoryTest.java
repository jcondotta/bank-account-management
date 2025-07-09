package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.mapper.BankAccountEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountDynamoDBRepositoryTest {

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Mock
    private BankAccountEntityMapper bankAccountEntityMapper;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankingEntity bankAccountEntityMock;

    @Mock
    private BankingEntity primaryAccountHolderEntityMock;

    @Mock
    private BankingEntity jointAccountHolderEntityMock;

    @Captor
    private ArgumentCaptor<TransactWriteItemsEnhancedRequest> requestArgumentCaptor;

    private CreateBankAccountDynamoDBRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CreateBankAccountDynamoDBRepository(dynamoDbEnhancedClient, bankingEntityDynamoDbTable, bankAccountEntityMapper);
    }

    @Test
    void shouldPersistBankAccount_whenBankAccountAndPrimaryAccountHolderAreValid() {
        var bankingEntities = List.of(bankAccountEntityMock, primaryAccountHolderEntityMock, jointAccountHolderEntityMock);

        when(bankAccountEntityMapper.toEntities(Mockito.any())).thenReturn(bankingEntities);
        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(TableSchema.fromBean(BankingEntity.class));
        repository.save(bankAccountMock);

        verify(dynamoDbEnhancedClient).transactWriteItems(requestArgumentCaptor.capture());

        TransactWriteItemsEnhancedRequest request = requestArgumentCaptor.getValue();

        assertThat(request.transactWriteItems())
            .hasSize(bankingEntities.size())
            .allSatisfy(item -> {
                assertThat(item.put()).isNotNull();
                assertThat(item.delete()).isNull();
                assertThat(item.update()).isNull();
            });

        verify(bankAccountEntityMapper).toEntities(bankAccountMock);
    }
}
