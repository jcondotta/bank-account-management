package com.jcondotta.infrastructure.ports.output.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankAccountQueryDynamoDBRepositoryTest {

//    private static final BankAccountId BANK_ACCOUNT_ID_BRAZIL = new BankAccountId(
//        TestBankAccountId.BRAZIL.getBankAccountId());
//
//    @Mock
//    private DynamoDbTable<BankingEntity> bankingEntitiesTable;
//
//    @Mock
//    private PageIterable<BankingEntity> pageIterable;
//
//    @Mock
//    private SdkIterable<BankingEntity> sdkIterable;
//
//    private BankAccountQueryDynamoDBRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        repository = new BankAccountQueryDynamoDBRepository(bankingEntitiesTable);
//    }
//
//    @Test
//    void shouldReturnBankAccountAndAccountHolders_whenExistentBankAccountIdProvided() {
//        when(bankingEntitiesTable.query(any(QueryConditional.class)))
//                .thenReturn(pageIterable);
//
//        when(pageIterable.items())
//                .thenReturn(sdkIterable);
//
//        var bankAccountEntity = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL.value());
//        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
//            TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL.value());
//
//        var expectedBankingEntities = Arrays.asList(bankAccountEntity, primaryAccountHolder);
//
//        when(sdkIterable.stream()).thenReturn(expectedBankingEntities.stream());
//
//        assertThat(repository.query(BANK_ACCOUNT_ID_BRAZIL))
//                .isNotEmpty()
//                .hasSize(expectedBankingEntities.size());
//
//        verify(bankingEntitiesTable).query(ArgumentMatchers.any(QueryConditional.class));
//    }
//
//    @Test
//    void shouldReturnEmptyList_whenNoBankAccountExistsByProvidedId() {
//        when(bankingEntitiesTable.query(any(QueryConditional.class)))
//                .thenReturn(pageIterable);
//
//        when(pageIterable.items())
//                .thenReturn(sdkIterable);
//
//        when(sdkIterable.stream()).thenReturn(Stream.empty());
//
//        assertThat(repository.query(BANK_ACCOUNT_ID_BRAZIL)).isEmpty();
//
//        verify(bankingEntitiesTable).query(any(QueryConditional.class));
//    }
}