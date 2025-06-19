package com.jcondotta;

import com.jcondotta.application.AccountHoldersQueryUseCase;
import com.jcondotta.domain.value_object.BankAccountId;
import com.jcondotta.application.dto.lookup.AccountHoldersQueryResponse;
import com.jcondotta.application.ports.input.service.AccountHoldersQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountHoldersQueryUseCaseImpl implements AccountHoldersQueryUseCase {

    private final AccountHoldersQueryService accountHoldersQueryService;

    @Override
    public AccountHoldersQueryResponse query(BankAccountId bankAccountId) {
        return accountHoldersQueryService.lookup(bankAccountId);
    }
}