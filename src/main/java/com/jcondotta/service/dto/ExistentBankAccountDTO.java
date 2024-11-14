package com.jcondotta.service.dto;

import com.jcondotta.domain.BankingEntity;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;

@Introspected
@Schema(name = "ExistentBankAccountDTO", description = "Represents an existing bank account entity returned when the bank account already exists.")
public class ExistentBankAccountDTO extends BankAccountDTO {

    public ExistentBankAccountDTO(BankingEntity bankAccount, BankingEntity primaryAccountHolder) {
        super(bankAccount, primaryAccountHolder);
    }
}