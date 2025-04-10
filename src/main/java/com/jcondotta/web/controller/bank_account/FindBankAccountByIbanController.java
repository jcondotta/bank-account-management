package com.jcondotta.web.controller.bank_account;

import com.jcondotta.service.bank_account.FindBankAccountByIbanService;
import com.jcondotta.service.dto.BankAccountDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Validated
@Controller(value = "${api.v1.bank-account-iban-path}")
public class FindBankAccountByIbanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBankAccountByIbanController.class);

    private final FindBankAccountByIbanService findBankAccountByIbanService;

    @Inject
    public FindBankAccountByIbanController(FindBankAccountByIbanService findBankAccountByIbanService) {
        this.findBankAccountByIbanService = findBankAccountByIbanService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<BankAccountDTO> findBankAccount(@PathVariable("iban") String bankAccountIban) {

        LOGGER.info("Received request to fetch bank account with Iban: {}", bankAccountIban);

        var bankAccountDTO = findBankAccountByIbanService.findBankAccountByIban(bankAccountIban);
        return HttpResponse.ok(bankAccountDTO);
    }
}
