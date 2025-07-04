package com.jcondotta.interfaces.rest.createbankaccount.model;

import com.jcondotta.interfaces.rest.shared.BankAccountDetailsRestResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a bank account entity with details.")
public record CreateBankAccountRestResponse(BankAccountDetailsRestResponse bankAccount) {}