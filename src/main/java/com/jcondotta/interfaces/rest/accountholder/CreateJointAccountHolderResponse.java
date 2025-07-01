package com.jcondotta.interfaces.rest.accountholder;

import com.jcondotta.interfaces.rest.AccountHolderDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Response containing information for the created joint account holder.")
public record CreateJointAccountHolderResponse(

    @NotNull
    AccountHolderDetailsResponse accountHolder

){ }