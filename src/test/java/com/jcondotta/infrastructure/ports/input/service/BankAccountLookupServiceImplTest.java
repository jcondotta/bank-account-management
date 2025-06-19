package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.application.dto.lookup.BankAccountLookupResponse;
import com.jcondotta.application.mapper.BankingEntityMapper;
import com.jcondotta.config.TestAccountHolderFactory;
import com.jcondotta.config.TestBankAccountFactory;
import com.jcondotta.config.TestClockConfig;
import com.jcondotta.domain.exception.BankAccountNotFoundException;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.infrastructure.ports.output.repository.BankAccountQueryDynamoDBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountLookupServiceImplTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
    private static final BankingEntityMapper BANKING_ENTITY_MAPPER = BankingEntityMapper.INSTANCE;

//    private BankingEntityLookupService<UUID, BankAccountLookupResponse> bankAccountLookupByIdService;
//
//    @Mock
//    private BankAccountQueryDynamoDBRepository repository;
//
//    @BeforeEach
//    public void beforeEach() {
//        bankAccountLookupByIdService = new BankAccountLookupServiceImpl(repository, BANKING_ENTITY_MAPPER);
//    }
//
//    @Test
//    void shouldReturnBankAccountWithPrimaryAccountHolder_whenExistingBankAccountIdIsProvided() {
//        var bankAccountEntity = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);
//        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
//        var expectedBankingEntities = Arrays.asList(bankAccountEntity, primaryAccountHolder);
//
//        when(repository.lookup(BANK_ACCOUNT_ID_BRAZIL)).thenReturn(expectedBankingEntities);
//
//        var lookupByIdResponse = bankAccountLookupByIdService.lookup(BANK_ACCOUNT_ID_BRAZIL);
//
//        assertAll(
//                () -> assertThat(lookupByIdResponse.bankAccountId().value()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL),
//                () -> assertThat(lookupByIdResponse.iban()).isNotBlank(),
//                () -> assertThat(lookupByIdResponse.dateOfOpening()).isEqualTo(LocalDateTime.now(TestClockConfig.testClockFixedInstant)),
//                () -> assertThat(lookupByIdResponse.accountHolders())
//                        .hasSize(1)
//                        .first()
//                        .satisfies(accountHolderDTO -> {
//                            assertThat(accountHolderDTO.getAccountHolderId().value()).isEqualTo(primaryAccountHolder.getAccountHolderId());
//                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(primaryAccountHolder.getAccountHolderName());
//                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(primaryAccountHolder.getPassportNumber());
//                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(primaryAccountHolder.getDateOfBirth());
//                        })
//        );
//
//        verify(repository).lookup(BANK_ACCOUNT_ID_BRAZIL);
//    }
//
//    @Test
//    void shouldReturnBankAccountWithAccountHolders_whenExistingBankAccountIdIsProvided() {
//        var bankAccountEntity = TestBankAccountFactory.create(BANK_ACCOUNT_ID_BRAZIL);
//        var primaryAccountHolder = TestAccountHolderFactory.createPrimaryAccountHolder(TestAccountHolderRequest.JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
//        var jointAccountHolder = TestAccountHolderFactory.createJointAccountHolder(TestAccountHolderRequest.PATRIZIO, BANK_ACCOUNT_ID_BRAZIL);
//
//        var expectedBankingEntities = Arrays.asList(bankAccountEntity, primaryAccountHolder, jointAccountHolder);
//
//        when(repository.lookup(BANK_ACCOUNT_ID_BRAZIL)).thenReturn(expectedBankingEntities);
//
//        var lookupByIdResponse = bankAccountLookupByIdService.lookup(BANK_ACCOUNT_ID_BRAZIL);
//
//        assertAll(
//                () -> assertThat(lookupByIdResponse.bankAccountId().value()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL),
//                () -> assertThat(lookupByIdResponse.iban()).isNotBlank(),
//                () -> assertThat(lookupByIdResponse.dateOfOpening()).isEqualTo(LocalDateTime.now(TestClockConfig.testClockFixedInstant)),
//                () -> assertThat(lookupByIdResponse.accountHolders())
//                        .hasSize(2)
//                        .anySatisfy(accountHolderDTO -> {
//                            assertThat(accountHolderDTO.getAccountHolderId().value()).isEqualTo(primaryAccountHolder.getAccountHolderId());
//                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(primaryAccountHolder.getAccountHolderName());
//                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(primaryAccountHolder.getPassportNumber());
//                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(primaryAccountHolder.getDateOfBirth());
//                        })
//                        .anySatisfy(accountHolderDTO -> {
//                            assertThat(accountHolderDTO.getAccountHolderId().value()).isEqualTo(jointAccountHolder.getAccountHolderId());
//                            assertThat(accountHolderDTO.getAccountHolderName()).isEqualTo(jointAccountHolder.getAccountHolderName());
//                            assertThat(accountHolderDTO.getPassportNumber()).isEqualTo(jointAccountHolder.getPassportNumber());
//                            assertThat(accountHolderDTO.getDateOfBirth()).isEqualTo(jointAccountHolder.getDateOfBirth());
//                        })
//        );
//    }
//
//    @Test
//    void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
//        when(repository.lookup(BANK_ACCOUNT_ID_BRAZIL)).thenReturn(List.of());
//
//        assertThatThrownBy(() -> bankAccountLookupByIdService.lookup(BANK_ACCOUNT_ID_BRAZIL))
//                .isInstanceOf(BankAccountNotFoundException.class)
//                .hasMessage("bankAccount.notFound")
//                .extracting("identifier")
//                    .isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
//
//        verify(repository).lookup(BANK_ACCOUNT_ID_BRAZIL);
//    }
}