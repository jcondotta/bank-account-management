package com.jcondotta.infrastructure.ports.input.service;

import com.jcondotta.application.ports.input.service.BankAccountIbanGeneratorService;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class FakerBankAccountIbanGeneratorService implements BankAccountIbanGeneratorService {

    private static final Faker faker = new Faker();

    @Override
    public String generateIban() {
        return faker.finance().iban();
    }
}
