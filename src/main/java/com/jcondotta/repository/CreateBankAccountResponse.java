package com.jcondotta.repository;

import com.jcondotta.service.dto.BankAccountDTO;

public record CreateBankAccountResponse(BankAccountDTO bankAccountDTO, boolean isIdempotent) {

    public static Builder builder(BankAccountDTO bankAccountDTO) {
        return new Builder(bankAccountDTO);
    }

    public static class Builder {

        private final BankAccountDTO bankAccountDTO;
        private boolean isIdempotent = false;

        private Builder(BankAccountDTO bankAccountDTO) {
            this.bankAccountDTO = bankAccountDTO;
        }

        public Builder isIdempotent(boolean isIdempotent) {
            this.isIdempotent = isIdempotent;
            return this;
        }

        public CreateBankAccountResponse build() {
            return new CreateBankAccountResponse(bankAccountDTO, isIdempotent);
        }
    }
}