package com.jcondotta.infrastructure.ports.output.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHolderDynamoDBRepositoryTest {

//    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
//
//    @InjectMocks
//    private CreateJointAccountHolderDynamoDBRepository createJointAccountHolderDynamoDBRepository;
//
//    @Mock
//    private DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
//
//    @Test
//    void shouldCreateJointAccountHolderAccountHolder_whenAccountHolderIsValid() {
//        var jeffersonAccountHolder = TestAccountHolderFactory
//                .createJointAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
//
//        createJointAccountHolderDynamoDBRepository.save(jeffersonAccountHolder);
//
//        verify(bankingEntityDynamoDbTable).putItem(jeffersonAccountHolder);
//        verifyNoMoreInteractions(bankingEntityDynamoDbTable);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenAccountHolderIsNull() {
//        var exception = assertThrows(NullPointerException.class, () -> createJointAccountHolderDynamoDBRepository.save(null));
//        assertThat(exception.getMessage()).isEqualTo("accountHolder.notNull");
//
//        verifyNoInteractions(bankingEntityDynamoDbTable);
//    }
}