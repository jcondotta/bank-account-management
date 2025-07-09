package com.jcondotta.application.usecase.createbankaccount.mapper;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountResult;
import com.jcondotta.application.usecase.shared.mapper.BankAccountDetailsMapper;
import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreateBankAccountResultMapperImplTest {

    private BankAccountDetailsMapper bankAccountDetailsMapper;
    private CreateBankAccountResultMapper mapper;

    @BeforeEach
    void setUp() {
        bankAccountDetailsMapper = mock(BankAccountDetailsMapper.class);
        mapper = new CreateBankAccountResultMapperImpl(bankAccountDetailsMapper);
    }

    @Test
    void shouldMapBankAccountToResult() {
        BankAccount fakeBankAccount = mock(BankAccount.class);
        BankAccountDetails expectedDetails = mock(BankAccountDetails.class);

        when(bankAccountDetailsMapper.toBankAccountDetails(fakeBankAccount)).thenReturn(expectedDetails);

        CreateBankAccountResult result = mapper.toResult(fakeBankAccount);

        assertThat(result).isNotNull();
        assertThat(result.bankAccount()).isEqualTo(expectedDetails);
        verify(bankAccountDetailsMapper).toBankAccountDetails(fakeBankAccount);
    }
}
