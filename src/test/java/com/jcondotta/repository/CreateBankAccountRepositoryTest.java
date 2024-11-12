package com.jcondotta.repository;

import com.jcondotta.domain.AccountHolder;
import com.jcondotta.domain.BankAccount;
import com.jcondotta.factory.AccountHolderTestFactory;
import com.jcondotta.factory.BankAccountTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.service.dto.BankAccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import static org.assertj.core.api.Assertions.assertThat;
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
    private DynamoDbTable<BankAccount> bankAccountDynamoDBTable;

    @Mock
    private DynamoDbTable<AccountHolder> accountHolderDynamoDBTable;

    @Mock
    private TableSchema<BankAccount> bankAccountSchema;

    @Mock
    private TableSchema<AccountHolder> accountHolderSchema;

    @Mock
    private TableMetadata bankAccountTableMetadata;

    @Mock
    private TableMetadata accountHolderTableMetadata;

    @BeforeEach
    public void beforeEach(){
//        when(bankAccountDynamoDBTable.tableSchema()).thenReturn(bankAccountSchema);
//        when(bankAccountSchema.itemType()).thenReturn(EnhancedType.of(BankAccount.class));
//        when(bankAccountSchema.tableMetadata()).thenReturn(bankAccountTableMetadata);

//        when(accountHolderDynamoDBTable.tableSchema()).thenReturn(accountHolderSchema);
//        when(accountHolderSchema.itemType()).thenReturn(EnhancedType.of(AccountHolder.class));
//        when(accountHolderSchema.tableMetadata()).thenReturn(accountHolderTableMetadata);
    }

//    @Test
//    void shouldSaveBankAccount_whenRecipientIsValid() {
//        var bankAccount = BankAccountTestFactory.create();
//        var jeffersonAccountHolder = AccountHolderTestFactory.createPrimaryAccountHolder(
//                bankAccount.getBankAccountId(),
//                TestAccountHolderRequest.JEFFERSON
//        );
//
//        var createBankAccountResponse = createBankAccountRepository.create(bankAccount, jeffersonAccountHolder);
//
//        var expectedBankAccountDTO = new BankAccountDTO(bankAccount, jeffersonAccountHolder);
//        assertThat(createBankAccountResponse.bankAccountDTO())
//                .usingRecursiveComparison()
//                .isEqualTo(expectedBankAccountDTO);
//
//        verify(dynamoDbEnhancedClient).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));
//        verifyNoMoreInteractions(dynamoDbEnhancedClient);
//    }
//
//    @Test
//    void shouldThrowException_whenDynamoDBThrowsException() {
//        var bankAccount = BankAccountTestFactory.create();
//        var jeffersonAccountHolder = AccountHolderTestFactory.createPrimaryAccountHolder(
//                bankAccount.getBankAccountId(),
//                TestAccountHolderRequest.JEFFERSON
//        );
//
//        var exceptionMessage = "Intentional dynamoDB exception message";
//        doThrow(DynamoDbException.builder().message(exceptionMessage).build())
//                .when(dynamoDbEnhancedClient).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));
//
//        var dynamoDBException = assertThrows(DynamoDbException.class, () -> createBankAccountRepository.create(bankAccount, jeffersonAccountHolder));
//        assertThat(dynamoDBException).hasMessage(exceptionMessage);
//
//        verify(dynamoDbEnhancedClient, times(1)).transactWriteItems(any(TransactWriteItemsEnhancedRequest.class));
//        verifyNoMoreInteractions(dynamoDbEnhancedClient);
//    }
}
