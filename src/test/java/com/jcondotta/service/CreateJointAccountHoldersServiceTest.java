package com.jcondotta.service;


import com.jcondotta.domain.BankingEntity;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import com.jcondotta.repository.CreateJointAccountHolderRepository;
import com.jcondotta.service.bank_account.CreateJointAccountHolderService;
import com.jcondotta.service.request.CreateJointAccountHoldersRequest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateJointAccountHoldersServiceTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
    private static final String PASSPORT_NUMBER_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getPassportNumber();
    private static final LocalDate DATE_OF_BIRTH_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getDateOfBirth();

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Mock
    private CreateJointAccountHolderRepository repository;

    private CreateJointAccountHolderService createJointAccountHolderService;

    @BeforeEach
    void beforeEach() {
        createJointAccountHolderService = new CreateJointAccountHolderService(repository, VALIDATOR);
    }

    @Test
    void shouldCreateJointAccountHolder_whenRequestAccountHoldersListHasOneItem() {
        var accountHolderRequest = TestAccountHolderRequest.JEFFERSON.toAccountHolderRequest();
        var createJointAccountHoldersRequest = new CreateJointAccountHoldersRequest(BANK_ACCOUNT_ID_BRAZIL, accountHolderRequest);

        createJointAccountHolderService.create(createJointAccountHoldersRequest);

        var bankingEntityCaptor = ArgumentCaptor.forClass(List.class);
        verify(repository).create(bankingEntityCaptor.capture());

        List<BankingEntity> capturedBankingEntities = bankingEntityCaptor.getValue();
        assertThat(capturedBankingEntities)
                .hasSize(1)
                .first()
                .satisfies(bankingEntity -> {
                    assertThat(bankingEntity.getAccountHolderId()).isNotNull();
                    assertThat(bankingEntity.getAccountHolderName()).isEqualTo(accountHolderRequest.accountHolderName());
                    assertThat(bankingEntity.getPassportNumber()).isEqualTo(accountHolderRequest.passportNumber());
                    assertThat(bankingEntity.getDateOfBirth()).isEqualTo(accountHolderRequest.dateOfBirth());
                    assertThat(bankingEntity.getBankAccountId()).isEqualTo(createJointAccountHoldersRequest.bankAccountId());
                });

        assertThat(capturedBankingEntities)
                .extracting(BankingEntity::getIban, BankingEntity::getDateOfOpening)
                .containsExactly(tuple(null, null));
    }
}