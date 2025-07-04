package com.jcondotta.application.usecase.shared.model;

import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderTypeValue;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;
import com.jcondotta.domain.accountholder.valueobjects.PassportNumber;
import com.jcondotta.domain.shared.valueobjects.CreatedAt;

public record AccountHolderDetails(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    DateOfBirth dateOfBirth,
    PassportNumber passportNumber,
    AccountHolderTypeValue accountHolderType,
    CreatedAt createdAt
) {}
