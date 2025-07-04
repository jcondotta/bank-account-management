package com.jcondotta.application.usecase.createbankaccount.mapper;

import com.jcondotta.application.usecase.createbankaccount.model.CreateBankAccountCommand;
import com.jcondotta.application.usecase.shared.mapper.AccountHolderMapper;
import com.jcondotta.domain.bankaccount.enums.AccountStatus;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.domain.bankaccount.valueobjects.AccountStatusValue;
import com.jcondotta.domain.bankaccount.valueobjects.BankAccountId;
import com.jcondotta.domain.bankaccount.valueobjects.Iban;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;

@Component
@AllArgsConstructor
public class CreateBankAccountCommandMapperImpl implements CreateBankAccountCommandMapper {

    private final AccountHolderMapper accountHolderMapper;
    private final Clock clock;

    private static final Faker FAKER = new Faker();

    public BankAccount toBankAccount(CreateBankAccountCommand command) {
        return new BankAccount(
            BankAccountId.newId(),
            command.accountType(),
            command.currency(),
            Iban.of(FAKER.finance().iban()),
            AccountStatusValue.pending(),
            CreatedAt.now(clock),
            List.of(accountHolderMapper
                .toPrimaryAccountHolder(command.createAccountHolderData(), clock)
            )
        );
    }
}
