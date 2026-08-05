package org.our_place.identity.infra.controller.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyUserContactRequest(
        @NotBlank String contactTypeCode,
        @NotBlank String value
) {}