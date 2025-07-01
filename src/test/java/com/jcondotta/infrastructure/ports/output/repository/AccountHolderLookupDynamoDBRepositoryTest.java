package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.application.ports.output.repository.AccountHolderLookupRepository;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.helper.TestBankAccountId;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountHolderLookupDynamoDBRepositoryTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    @Mock
    private DynamoDbTable<BankingEntity> bankingEntitiesTable;

    @Mock
    private PageIterable<BankingEntity> pageIterable;

    @Mock
    private SdkIterable<BankingEntity> sdkIterable;

    private AccountHolderLookupRepository repository;

//    @BeforeEach
//    void beforeEach() {
//        repository = new AccountHolderLookupDynamoDBRepository(bankingEntitiesTable);
//    }
//
//    @Test
//    void shouldLookupAccountHolders_whenValidBankAccountIdProvided_andEntitiesExist() {
//        when(bankingEntitiesTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
//        when(pageIterable.items()).thenReturn(sdkIterable);
//
//        var primaryAccountHolder = TestAccountHolderFactory
//            .createPrimaryAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
//
//        when(sdkIterable.stream()).thenReturn(expectedBankingEntities.stream());
//
//        assertThat(repository.lookup(any()))
//            .isNotNull()
//
//            .isNotEmpty()
//            .hasSize(expectedBankingEntities.size());
//
//        verify(bankingEntitiesTable).query(any(QueryConditional.class));
//    }
//
//    @Test
//    void shouldReturnEmptyList_whenNoEntitiesFound_forProvidedBankAccountId() {
//        when(bankingEntitiesTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
//        when(pageIterable.items()).thenReturn(sdkIterable);
//        when(sdkIterable.stream()).thenReturn(Stream.empty());
//
//        assertThat(repository.lookup(BANK_ACCOUNT_ID_BRAZIL)).isEmpty();
//
//        verify(bankingEntitiesTable).query(any(QueryConditional.class));
//    }
}