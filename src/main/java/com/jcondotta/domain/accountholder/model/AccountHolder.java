package com.jcondotta.domain.accountholder.model;

import com.jcondotta.domain.shared.valueobjects.CreatedAt;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderId;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderName;
import com.jcondotta.domain.accountholder.valueobjects.AccountHolderTypeValue;
import com.jcondotta.domain.accountholder.valueobjects.DateOfBirth;

public record AccountHolder(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    String passportNumber,
    DateOfBirth dateOfBirth,
    AccountHolderTypeValue accountHolderType,
    CreatedAt createdAt
) {}
