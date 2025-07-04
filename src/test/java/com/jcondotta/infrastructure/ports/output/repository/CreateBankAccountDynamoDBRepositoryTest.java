package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import com.jcondotta.infrastructure.adapters.persistence.repository.CreateBankAccountDynamoDBRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableMetadata;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountDynamoDBRepositoryTest {

    @InjectMocks
    private CreateBankAccountDynamoDBRepository createBankAccountRepository;

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Mock
    private TableSchema<BankingEntity> bankingEntityTableSchema;

    @Mock
    private TableMetadata tableMetadata;

//    @Test
//    void shouldSaveBankAccount_whenRequestIsValid() {
//        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
//        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
//        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);
//
//        var bankAccountDetails = TestBankAccountFactory.create();
//        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
//                TestAccountHolderRequest.JEFFERSON,
//                bankAccountDetails.getBankAccountId()
//        );
//
////        createBankAccountRepository.save(bankAccountDetails);
//
//        var argumentCaptor = ArgumentCaptor.forClass(TransactWriteItemsEnhancedRequest.class);
//        verify(dynamoDbEnhancedClient).transactWriteItems(argumentCaptor.capture());
//
//        var argumentCaptorValue = argumentCaptor.getValue();
//        assertThat(argumentCaptorValue).isNotNull();
//
//        var transactWriteItems = argumentCaptorValue.transactWriteItems();
//        assertThat(transactWriteItems).hasSize(2);
//
//        assertAll(
//                () -> assertThat(transactWriteItems.get(0).put()).isNotNull(),
//                () -> assertThat(transactWriteItems.get(1).put()).isNotNull()
//        );
//
//        verifyNoMoreInteractions(dynamoDbEnhancedClient);
//    }
//
//    @Test
//    void shouldThrowException_whenDynamoDBTransactionFails() {
//        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
//        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
//        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);
//
//        var bankAccountDetails = TestBankAccountFactory.create();
//        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
//                TestAccountHolderRequest.JEFFERSON,
//                bankAccountDetails.getBankAccountId()
//        );
//        var exceptionMessage = "DynamoDB Transaction Failed";
//
//        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
//                .when(dynamoDbEnhancedClient).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));
//
//        var exception = assertThrows(DynamoDbException.class, () ->
//                createBankAccountRepository.createBankAccount(bankAccountDetails, jeffersonAccountHolder)
//        );
//
//        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
//        verify(dynamoDbEnhancedClient).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));
//        verifyNoMoreInteractions(dynamoDbEnhancedClient);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenBankAccountIsNull() {
//        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
//                TestAccountHolderRequest.JEFFERSON, UUID.randomUUID()
//        );
//
//        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.createBankAccount(null, jeffersonAccountHolder));
//        assertThat(exception.getMessage()).isEqualTo("bankAccountDetails.notNull");
//
//        verifyNoInteractions(dynamoDbEnhancedClient);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
//        var bankAccountDetails = TestBankAccountFactory.create();
//
//        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.createBankAccount(bankAccountDetails, (BankingEntity) null));
//        assertThat(exception.getMessage()).isEqualTo("accountHolder.notNull");
//
//        verifyNoInteractions(dynamoDbEnhancedClient);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenAccountHolderListIsNull() {
//        var bankAccountDetails = TestBankAccountFactory.create();
//
//        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.createBankAccount(bankAccountDetails, (List<BankingEntity>) null));
//        assertThat(exception.getMessage()).isEqualTo("accountHolders.notNull");
//
//        verifyNoInteractions(dynamoDbEnhancedClient);
//    }
}
