package com.jcondotta;

import com.jcondotta.application.CreateBankAccountUseCase;
import com.jcondotta.application.dto.create.CreateBankAccountRequest;
import com.jcondotta.application.dto.create.CreateBankAccountResponse;
import com.jcondotta.application.ports.input.service.CreateBankAccountService;
import com.jcondotta.infrastructure.ports.input.service.CreateBankAccountServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateBankAccountUseCaseImpl implements CreateBankAccountUseCase {

    private final CreateBankAccountService createBankAccountService;

    public CreateBankAccountUseCaseImpl(CreateBankAccountServiceImpl createBankAccountService) {
        this.createBankAccountService = createBankAccountService;
    }

    @Override
    public CreateBankAccountResponse createBankAccount(CreateBankAccountRequest request) {
        return createBankAccountService.createBankAccount(request);
    }
}
