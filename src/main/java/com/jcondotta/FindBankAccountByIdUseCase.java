package com.jcondotta;

import com.jcondotta.service.bank_account.FindBankAccountService;
import com.jcondotta.service.dto.BankAccountDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindBankAccountByIdUseCase {

    private final FindBankAccountService findBankAccountService;
    // Aws SNS Handler =D

    public FindBankAccountByIdUseCase(FindBankAccountService findBankAccountService) {
        this.findBankAccountService = findBankAccountService;
    }

    public BankAccountDTO findBankAccountById(UUID bankAccountId) {
        return findBankAccountService.findBankAccountById(bankAccountId);

        //Create bank account logic + send message to SNS topic

        //        snsTopicPublisher.publishMessage(bankAccountDTO.getPrimaryAccountHolder()
//                .orElseThrow(() -> new IllegalStateException("Primary account holder was not found for the created bank account: "
//                        + bankAccountDTO.getBankAccountId())));
    }
}
