package com.jcondotta;

import com.jcondotta.event.AccountHolderCreatedSNSTopicPublisher;
import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.dto.AccountHolderDTO;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBankAccountUseCaseTest {

    @Mock
    private CreateBankAccountService createBankAccountService;

    @Mock
    private AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;

    @InjectMocks
    private CreateBankAccountUseCase useCase;

    @Captor
    private ArgumentCaptor<AccountHolderDTO> primaryAccountHolderCaptor;

    @Test
    void shouldCreateBankAccount_andPublishEvent_whenPrimaryAccountHolderPresent() {
        // Arrange
        CreateAccountHolderRequest request = mock(CreateAccountHolderRequest.class);
        AccountHolderDTO primaryAccountHolder = mock(AccountHolderDTO.class);

        BankAccountDTO bankAccountDTO = mock(BankAccountDTO.class);
        when(bankAccountDTO.getPrimaryAccountHolder()).thenReturn(Optional.of(primaryAccountHolder));
        when(createBankAccountService.create(request)).thenReturn(bankAccountDTO);

        // Act
        BankAccountDTO result = useCase.createBankAccount(request);

        // Assert
        assertThat(result).isSameAs(bankAccountDTO);
        verify(createBankAccountService).create(request);
        verify(snsTopicPublisher).publishMessage(primaryAccountHolder);
        verifyNoMoreInteractions(createBankAccountService, snsTopicPublisher);
    }

    @Test
    void shouldCreateBankAccount_andNotPublishEvent_whenPrimaryAccountHolderAbsent() {
        // Arrange
        CreateAccountHolderRequest request = mock(CreateAccountHolderRequest.class);

        BankAccountDTO bankAccountDTO = mock(BankAccountDTO.class);
        when(bankAccountDTO.getPrimaryAccountHolder()).thenReturn(Optional.empty());
        when(createBankAccountService.create(request)).thenReturn(bankAccountDTO);

        // Act
        BankAccountDTO result = useCase.createBankAccount(request);

        // Assert
        assertThat(result).isSameAs(bankAccountDTO);
        verify(createBankAccountService).create(request);
        verify(snsTopicPublisher, never()).publishMessage(any());
        verifyNoMoreInteractions(createBankAccountService, snsTopicPublisher);
    }

    @Test
    void shouldPropagateException_whenCreateBankAccountServiceThrows() {
        // Arrange
        CreateAccountHolderRequest request = mock(CreateAccountHolderRequest.class);
        RuntimeException exception = new RuntimeException("DB error");

        when(createBankAccountService.create(request)).thenThrow(exception);

        // Act & Assert
        RuntimeException thrown = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                useCase.createBankAccount(request)
        );
        assertThat(thrown).isSameAs(exception);
        verify(createBankAccountService).create(request);
        verifyNoInteractions(snsTopicPublisher);
    }
}
