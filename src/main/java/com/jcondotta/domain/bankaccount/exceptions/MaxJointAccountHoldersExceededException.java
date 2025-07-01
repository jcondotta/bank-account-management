package com.jcondotta.domain.bankaccount.exceptions;

import lombok.Getter;

import static com.jcondotta.domain.shared.DomainErrorMessages.MAX_ACCOUNT_HOLDERS_LIMIT_REACHED;

@Getter
public class MaxJointAccountHoldersExceededException extends RuntimeException {

    private final int maxAllowed;

    public MaxJointAccountHoldersExceededException(int maxAllowed) {
        super(MAX_ACCOUNT_HOLDERS_LIMIT_REACHED);
        this.maxAllowed = maxAllowed;
    }
}