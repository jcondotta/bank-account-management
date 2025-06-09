package com.jcondotta;

import com.jcondotta.cache.CacheStore;
import com.jcondotta.event.AccountHolderCreatedSNSTopicPublisher;
import com.jcondotta.service.bank_account.CreateBankAccountService;
import com.jcondotta.service.dto.BankAccountDTO;
import com.jcondotta.service.request.CreateAccountHolderRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CreateBankAccountUseCase {

    private final CreateBankAccountService createBankAccountService;
    private final AccountHolderCreatedSNSTopicPublisher snsTopicPublisher;

    @Qualifier("bankAccountCacheService")
    private final CacheStore<String, BankAccountDTO> bankAccountCacheService;

    public CreateBankAccountUseCase(CreateBankAccountService createBankAccountService, AccountHolderCreatedSNSTopicPublisher snsTopicPublisher, CacheStore<String, BankAccountDTO> bankAccountCacheService) {
        this.createBankAccountService = createBankAccountService;
        this.snsTopicPublisher = snsTopicPublisher;
        this.bankAccountCacheService = bankAccountCacheService;
    }

    public BankAccountDTO createBankAccount(CreateAccountHolderRequest request) {
        var bankAccountDTO = createBankAccountService.create(request);

        bankAccountCacheService.put(bankAccountDTO.getBankAccountId().toString(), bankAccountDTO);

        bankAccountDTO.getPrimaryAccountHolder()
                .ifPresent(snsTopicPublisher::publishMessage);

        return bankAccountDTO;
    }
}
