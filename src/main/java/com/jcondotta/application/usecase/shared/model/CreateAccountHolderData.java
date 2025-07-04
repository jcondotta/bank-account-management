package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;

public record CreateAccountHolderData(
    AccountHolderName accountHolderName,
    DateOfBirth dateOfBirth,
    PassportNumber passportNumber
) {}
