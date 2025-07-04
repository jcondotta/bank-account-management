package com.jcondotta.infrastructure.adapters.persistence.mapper;

import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.persistence.entity.BankingEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class BankingEntityAssemblerMapper {

    private final BankAccountEntityMapper bankAccountEntityMapper;
    private final AccountHolderEntityMapper accountHolderEntityMapper;

    public List<BankingEntity> toEntities(BankAccount bankAccount) {
        var root = bankAccountEntityMapper.toEntity(bankAccount);
        var holders = bankAccount.accountHolders().stream()
            .map(holder -> accountHolderEntityMapper.toEntity(bankAccount.bankAccountId(), holder))
            .toList();

        return Stream.concat(Stream.of(root), holders.stream()).toList();
    }

    public BankAccount toBankAccount(BankingEntity bankAccountEntity, List<BankingEntity> accountHolderEntities, Clock clock) {
        return bankAccountEntityMapper.toDomain(bankAccountEntity, accountHolderEntities, accountHolderEntityMapper, clock);
    }
}