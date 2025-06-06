package com.jcondotta.service.bank_account;

import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.BankingEntity;
import com.jcondotta.domain.BankingEntityMapper;
import com.jcondotta.exception.ResourceNotFoundException;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindBankAccountServiceTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

    @Mock
    private DynamoDbTable<BankingEntity> dynamoDbTable;

    @Mock
    private PageIterable<BankingEntity> pageIterable;

    @Mock
    private SdkIterable<BankingEntity> sdkIterable;

    private FindBankAccountService findBankAccountService;

    @BeforeEach
    public void beforeEach() {
        findBankAccountService = new FindBankAccountService(dynamoDbTable, BANKING_ENTITY_MAPPER);
    }

    @Test
    void shouldReturnBankAccountWithPrimaryAccountHolder_whenExistingBankAccountIdIsProvided() {
        when(dynamoDbTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(sdkIterable);

        var bankAccountEntity = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);
        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);

        when(sdkIterable.stream()).thenReturn(Stream.of(bankAccountEntity, primaryAccountHolder));

        var bankAccountDTO = findBankAccountService.findBankAccountById(BANK_ACCOUNT_ID_BRAZIL);

        assertAll(
                () -> assertThat(bankAccountDTO.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL),
                () -> assertThat(bankAccountDTO.getIban()).isNotBlank(),
                () -> assertThat(bankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(TestClockConfig.testClockFixedInstant)),
                () -> assertThat(bankAccountDTO.getAccountHolders())
                        .hasSize(1)
                        .first()
                        .satisfies(accountHolderDTO -> {
                            assertThat(accountHolderDTO.getAccountHolderId()).isEqualTo(primaryAccountHolder.getAccountHolderId());
                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(primaryAccountHolder.getAccountHolderName());
                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(primaryAccountHolder.getPassportNumber());
                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(primaryAccountHolder.getDateOfBirth());
                        })
        );
    }

    @Test
    void shouldReturnBankAccountWithAccountHolders_whenExistingBankAccountIdIsProvided() {
        when(dynamoDbTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(sdkIterable);

        var bankAccountEntity = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);
        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
        var jointAccountHolder = TestAccountHolderFactory.createJointAccountHolder(TestAccountHolderRequest.PATRIZIO, BANK_ACCOUNT_ID_BRAZIL);

        when(sdkIterable.stream()).thenReturn(Stream.of(bankAccountEntity, primaryAccountHolder, jointAccountHolder));

        var bankAccountDTO = findBankAccountService.findBankAccountById(BANK_ACCOUNT_ID_BRAZIL);

        assertAll(
                () -> assertThat(bankAccountDTO.getBankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL),
                () -> assertThat(bankAccountDTO.getIban()).isNotBlank(),
                () -> assertThat(bankAccountDTO.getDateOfOpening()).isEqualTo(LocalDateTime.now(TestClockConfig.testClockFixedInstant)),
                () -> assertThat(bankAccountDTO.getAccountHolders())
                        .hasSize(2)
                        .anySatisfy(accountHolderDTO -> {
                            assertThat(accountHolderDTO.getAccountHolderId()).isEqualTo(primaryAccountHolder.getAccountHolderId());
                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(primaryAccountHolder.getAccountHolderName());
                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(primaryAccountHolder.getPassportNumber());
                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(primaryAccountHolder.getDateOfBirth());
                        })
                        .anySatisfy(accountHolderDTO -> {
                            assertThat(accountHolderDTO.getAccountHolderId()).isEqualTo(jointAccountHolder.getAccountHolderId());
                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(jointAccountHolder.getAccountHolderName());
                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(jointAccountHolder.getPassportNumber());
                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(jointAccountHolder.getDateOfBirth());
                        })
        );
    }

    @Test
    void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
        when(dynamoDbTable.query(any(QueryConditional.class))).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(sdkIterable);

        var nonExistentBankAccountId = TestBankAccountId.ITALY.getBankAccountId();

        assertThatThrownBy(() -> findBankAccountService.findBankAccountById(nonExistentBankAccountId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("bankAccount.notFound")
                .extracting("identifier")
                    .isEqualTo(nonExistentBankAccountId);

        verify(dynamoDbTable).query(any(QueryConditional.class));
    }
}