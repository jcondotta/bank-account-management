package com.jcondotta.application.dto.create;

import com.jcondotta.domain.value_object.AccountHolderId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Response containing information for the created joint account holder.")
public record CreateJointAccountHolderResponse(

    @NotNull
    AccountHolderId accountHolderId

){ }