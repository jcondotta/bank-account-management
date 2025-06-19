package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.domain.model.BankingEntity;
import com.jcondotta.helper.TestAccountHolderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldSaveBankAccount_whenRequestIsValid() {
        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);

        var bankAccount = TestBankAccountFactory.create();
        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );

        createBankAccountRepository.createBankAccount(bankAccount, jeffersonAccountHolder);

        var argumentCaptor = ArgumentCaptor.forClass(TransactWriteItemsEnhancedRequest.class);
        verify(dynamoDbEnhancedClient).transactWriteItems(argumentCaptor.capture());

        var argumentCaptorValue = argumentCaptor.getValue();
        assertThat(argumentCaptorValue).isNotNull();

        var transactWriteItems = argumentCaptorValue.transactWriteItems();
        assertThat(transactWriteItems).hasSize(2);

        assertAll(
                () -> assertThat(transactWriteItems.get(0).put()).isNotNull(),
                () -> assertThat(transactWriteItems.get(1).put()).isNotNull()
        );

        verifyNoMoreInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowException_whenDynamoDBTransactionFails() {
        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);

        var bankAccount = TestBankAccountFactory.create();
        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );
        var exceptionMessage = "DynamoDB Transaction Failed";

        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
                .when(dynamoDbEnhancedClient).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));

        var exception = assertThrows(DynamoDbException.class, () ->
                createBankAccountRepository.createBankAccount(bankAccount, jeffersonAccountHolder)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        verify(dynamoDbEnhancedClient).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));
        verifyNoMoreInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountIsNull() {
        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
                TestAccountHolderRequest.JEFFERSON, UUID.randomUUID()
        );

        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.createBankAccount(null, jeffersonAccountHolder));
        assertThat(exception.getMessage()).isEqualTo("bankAccount.notNull");

        verifyNoInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
        var bankAccount = TestBankAccountFactory.create();

        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.createBankAccount(bankAccount, (BankingEntity) null));
        assertThat(exception.getMessage()).isEqualTo("accountHolder.notNull");

        verifyNoInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderListIsNull() {
        var bankAccount = TestBankAccountFactory.create();

        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.createBankAccount(bankAccount, (List<BankingEntity>) null));
        assertThat(exception.getMessage()).isEqualTo("accountHolders.notNull");

        verifyNoInteractions(dynamoDbEnhancedClient);
    }
}
