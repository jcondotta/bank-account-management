package com.jcondotta.interfaces.rest.shared.mapper;

import com.jcondotta.application.usecase.shared.model.AccountHolderDetails;
import com.jcondotta.application.usecase.shared.model.BankAccountDetails;
import com.jcondotta.interfaces.rest.shared.AccountHolderDetailsRestResponse;
import com.jcondotta.interfaces.rest.shared.BankAccountDetailsRestResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BankAccountDetailsRestResponseMapperImpl implements BankAccountDetailsRestResponseMapper {

    private final AccountHolderDetailsRestResponseMapper accountHolderDetailsRestResponseMapper;

    @Override
    public BankAccountDetailsRestResponse toResponse(BankAccountDetails bankAccount) {
        return BankAccountDetailsRestResponse.builder()
            .bankAccountId(bankAccount.bankAccountId().value())
            .accountType(bankAccount.accountType())
            .currency(bankAccount.currency())
            .iban(bankAccount.iban().value())
            .dateOfOpening(bankAccount.dateOfOpening().asZonedDateTime())
            .status(bankAccount.accountStatus())
            .accountHolders(toAccountHolderResponses(bankAccount.accountHolders()))
            .build();
    }

    private List<AccountHolderDetailsRestResponse> toAccountHolderResponses(List<AccountHolderDetails> holders) {
        return holders.stream()
            .map(accountHolderDetailsRestResponseMapper::toResponse)
            .collect(Collectors.toList());
    }
}
