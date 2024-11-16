package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.TestAccountHolderFactory;
import com.jcondotta.factory.TestBankAccountFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.dto.BankAccountDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountRepositoryTest {

    @InjectMocks
    private CreateBankAccountRepository createBankAccountRepository;

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;

    @Mock
    private TableSchema<BankingEntity> bankingEntityTableSchema;

    @Mock
    private TableMetadata tableMetadata;

    @AfterEach
    void afterEach(){
        assertThat(MDC.get("bankAccountId"))
                .as("MDC should be cleared after the publishMessage method completes for bankAccountId")
                .isNull();
        assertThat(MDC.get("accountHolderId"))
                .as("MDC should be cleared after the publishMessage method completes for accountHolderId")
                .isNull();
    }

    @Test
    void shouldSaveBankAccount_whenRecipientIsValid() {
        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);

        var bankAccount = TestBankAccountFactory.create();
        var jeffersonAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(
                TestAccountHolderRequest.JEFFERSON,
                bankAccount.getBankAccountId()
        );

        var createBankAccountResponse = createBankAccountRepository.create(bankAccount, jeffersonAccountHolder);

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

        var expectedBankAccountDTO = new BankAccountDTO(bankAccount, jeffersonAccountHolder);
        assertThat(createBankAccountResponse.bankAccountDTO())
                .usingRecursiveComparison()
                .isEqualTo(expectedBankAccountDTO);

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
                createBankAccountRepository.create(bankAccount, jeffersonAccountHolder)
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

        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.create(null, jeffersonAccountHolder));
        assertThat(exception.getMessage()).isEqualTo("bankAccount.notNull");

        verifyNoInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
        var bankAccount = TestBankAccountFactory.create();

        var exception = assertThrows(NullPointerException.class, () -> createBankAccountRepository.create(bankAccount, null));
        assertThat(exception.getMessage()).isEqualTo("accountHolder.notNull");

        verifyNoInteractions(dynamoDbEnhancedClient);
    }
}
