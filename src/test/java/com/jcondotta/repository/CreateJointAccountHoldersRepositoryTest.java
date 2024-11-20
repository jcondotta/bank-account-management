package com.jcondotta.repository;

import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.TestAccountHolderFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHoldersRepositoryTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    @InjectMocks
    private CreateJointAccountHoldersRepository createJointAccountHoldersRepository;

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
    void shouldCreateAccountHolder_whenRequestHasOneAccountHolder() {
        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);

        var jeffersonAccountHolder = TestAccountHolderFactory.createJointAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);

        var jointAccountHolders = List.of(jeffersonAccountHolder);
        createJointAccountHoldersRepository.create(jointAccountHolders);

        var argumentCaptor = ArgumentCaptor.forClass(TransactWriteItemsEnhancedRequest.class);
        verify(dynamoDbEnhancedClient).transactWriteItems(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue())
                .satisfies(transactWriteItemsEnhancedRequest -> assertAll(
                        () -> assertThat(transactWriteItemsEnhancedRequest.transactWriteItems()).hasSize(1),
                        () -> assertThat(transactWriteItemsEnhancedRequest.transactWriteItems().get(0).put()).isNotNull()
                ));

        verifyNoMoreInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldCreateAccountHolders_whenRequestHasMultipleAccountHolders() {
        when(bankingEntityDynamoDbTable.tableSchema()).thenReturn(bankingEntityTableSchema);
        when(bankingEntityTableSchema.itemType()).thenReturn(EnhancedType.of(BankingEntity.class));
        when(bankingEntityTableSchema.tableMetadata()).thenReturn(tableMetadata);

        var jeffersonAccountHolder = TestAccountHolderFactory.createJointAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
        var patricioAccountHolder = TestAccountHolderFactory.createJointAccountHolder(TestAccountHolderRequest.PATRIZIO, BANK_ACCOUNT_ID_BRAZIL);

        var jointAccountHolders = List.of(jeffersonAccountHolder, patricioAccountHolder);
        createJointAccountHoldersRepository.create(jointAccountHolders);

        var argumentCaptor = ArgumentCaptor.forClass(TransactWriteItemsEnhancedRequest.class);
        verify(dynamoDbEnhancedClient).transactWriteItems(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue())
                .satisfies(transactWriteItemsEnhancedRequest -> assertAll(
                        () -> assertThat(transactWriteItemsEnhancedRequest.transactWriteItems()).hasSize(2),
                        () -> assertThat(transactWriteItemsEnhancedRequest.transactWriteItems().get(0).put()).isNotNull(),
                        () -> assertThat(transactWriteItemsEnhancedRequest.transactWriteItems().get(1).put()).isNotNull()
                ));

        verifyNoMoreInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenAccountHoldersListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> createJointAccountHoldersRepository.create(new ArrayList<>()));

        verifyNoInteractions(dynamoDbEnhancedClient);
    }

    @Test
    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
        var exception = assertThrows(NullPointerException.class, () -> createJointAccountHoldersRepository.create(null));
        assertThat(exception.getMessage()).isEqualTo("accountHolders.notNull");

        verifyNoInteractions(bankingEntityDynamoDbTable);
    }
}