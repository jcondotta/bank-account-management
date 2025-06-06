package com.jcondotta;

import com.jcondotta.event.AccountHolderCreatedSNSTopicPublisher;
import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateBankAccountUseCase {

    private final CreateBankAccountService createBankAccountService;
    private final AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;

    public CreateBankAccountUseCase(CreateBankAccountService createBankAccountService, AccountHolderCreatedSNSTopicPublisher snsTopicPublisher) {
        this.createBankAccountService = createBankAccountService;
        this.snsTopicPublisher = snsTopicPublisher;
    }

    public BankAccountDTO createBankAccount(CreateAccountHolderRequest request) {
        var bankAccountDTO = createBankAccountService.create(request);

        bankAccountDTO.getPrimaryAccountHolder()
                .ifPresent(snsTopicPublisher::publishMessage);

        return bankAccountDTO;
    }
}
