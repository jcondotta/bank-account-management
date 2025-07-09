package com.jcondotta.infrastructure.adapters.persistence.repository;

import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.enums.EntityType;
import com.jcondotta.infrastructure.adapters.persistence.mapper.BankAccountEntityMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.PaginatedItemsIterable;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.Clock;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LookupDynamoDBBankAccountRepositoryTest {

    private static final Clock FIXED_CLOCK = TestClockConfig.FIXED_CLOCK;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankingEntity bankAccountEntityMock;

    @Mock
    private BankingEntity primaryAccountHolderEntityMock;

    @Mock
    private BankingEntity jointAccountHolderEntityMock;

    @Mock
    private DynamoDbTable<BankingEntity> dynamoDbTable;

    @Mock
    private BankAccountEntityMapper bankAccountEntityMapper;

    @Mock
    private PageIterable<BankingEntity> pageIterable;

    @Mock
    private Page<BankingEntity> bankingEntityPage;

    private LookupDynamoDBBankAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LookupDynamoDBBankAccountRepository(dynamoDbTable, bankAccountEntityMapper);
    }

    @Test
    void shouldReturnBankAccount_whenEntityIsFound() {
        when(bankAccountEntityMock.isEntityTypeBankAccount()).thenReturn(true);
        when(primaryAccountHolderEntityMock.isEntityTypeAccountHolder()).thenReturn(true);
        when(jointAccountHolderEntityMock.isEntityTypeAccountHolder()).thenReturn(true);

        var bankingEntities = List.of(bankAccountEntityMock, primaryAccountHolderEntityMock, jointAccountHolderEntityMock);

        when(dynamoDbTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(bankingEntities::iterator);
        when(bankAccountEntityMapper.toDomain(any(), any())).thenReturn(bankAccountMock);

        assertThat(repository.lookup(BankAccountId.newId())).isNotEmpty();

        verify(dynamoDbTable).query(any(QueryConditional.class));
        verify(bankAccountEntityMapper).toDomain(any(), any());
    }

    @Test
    void shouldReturnEmpty_whenNoBankAccountEntityIsFound() {
        when(dynamoDbTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(Collections::emptyIterator);

        assertThat(repository.lookup(BankAccountId.newId())).isEmpty();

        verify(dynamoDbTable).query(any(QueryConditional.class));
        verifyNoInteractions(bankAccountEntityMapper);
    }
}
