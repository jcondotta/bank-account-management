package com.jcondotta.infrastructure.ports.output.repository;

import com.jcondotta.application.ports.output.repository.AccountHolderLookupRepository;
import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.domain.model.BankingEntity;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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