package com.jcondotta.service.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record AccountHoldersDTO(List<AccountHolderDTO> accountHolders) {
}
