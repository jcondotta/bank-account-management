package com.jcondotta.service.bank_account;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountUseCaseImplTest {

//    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
//
//    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;
//
//    @Mock
//    private BankAccountDTO bankAccountDTOMock;
//
//    @Mock
//    private AccountHolderDTO accountHolderDTOMock;
//
//    @Mock
//    private BankAccountApplicationServiceImpl createBankAccountService;
//
//    @Mock
//    private AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;
//
//    @Mock
//    private CacheStore<String, BankAccountDTO> cacheStore;
//
//    @InjectMocks
//    private CreateBankAccountUseCaseImpl createBankAccountUseCase;
//
//    @Test
//    void shouldCreateBankAccount_and_PublishEvent_whenRequestIsValid() {
//        var createAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//
//        when(bankAccountDTOMock.getPrimaryAccountHolder()).thenReturn(Optional.of(accountHolderDTOMock));
//        when(createBankAccountService.createBankAccount(any())).thenReturn(bankAccountDTOMock);
//
//        createBankAccountUseCase.createBankAccount(createAccountHolderRequest);
//
//        verify(createBankAccountService).createBankAccount(createAccountHolderRequest);
//        verify(cacheStore).put(any(), eq(bankAccountDTOMock));
//        verify(snsTopicPublisher).publishMessage(any());
//        verifyNoMoreInteractions(createBankAccountService, snsTopicPublisher);
//    }
//
//    @Test
//    void shouldCreateBankAccount_and_NotPublishEvent_whenPrimaryAccountHolderIsAbsent() {
//        var createAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//
//        when(createBankAccountService.createBankAccount(any())).thenReturn(bankAccountDTOMock);
//
//        createBankAccountUseCase.createBankAccount(createAccountHolderRequest);
//
//        verify(createBankAccountService).createBankAccount(createAccountHolderRequest);
//        verify(cacheStore).put(any(), eq(bankAccountDTOMock));
//        verify(snsTopicPublisher, never()).publishMessage(any());
//        verifyNoMoreInteractions(createBankAccountService, snsTopicPublisher);
//    }
//
//    @Test
//    void shouldNotCreateBankAccountNorPublishEvent_whenCreateBankAccountServiceThrowsException() {
//        var createAccountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
//        var intentionalExceptionMessage = "Intentional exception message";
//
//        when(createBankAccountService.createBankAccount(any()))
//                .thenThrow(new RuntimeException(intentionalExceptionMessage));
//
//        assertThatThrownBy(() -> createBankAccountUseCase.createBankAccount(createAccountHolderRequest))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining(intentionalExceptionMessage);
//
//        verify(createBankAccountService).createBankAccount(createAccountHolderRequest);
//        verify(cacheStore, never()).put(any(), eq(bankAccountDTOMock));
//        verifyNoInteractions(snsTopicPublisher);
//        verifyNoMoreInteractions(createBankAccountService);
//    }
}