package com.jcondotta.domain.bankaccount.exceptions;

import com.jcondotta.domain.shared.exceptions.BusinessRuleException;
import lombok.Getter;

import java.util.Map;

import static com.jcondotta.domain.shared.DomainErrorMessages.MAX_ACCOUNT_HOLDERS_LIMIT_REACHED;

@Getter
public class MaxJointAccountHoldersExceededException extends BusinessRuleException {

    private final int limit;
    private final int current;

    public MaxJointAccountHoldersExceededException(int limit, int current) {
        super(MAX_ACCOUNT_HOLDERS_LIMIT_REACHED);
        this.limit = limit;
        this.current = current;
    }

    @Override
    public String getType() {
        return "/problems/max-joint-account-holders-exceeded";
    }

    @Override
    public Map<String, Object> getProperties() {
        return Map.of(
            "limit", limit,
            "current", current
        );
    }
}