package org.our_place.room.infra.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record SetRelationshipRequest(
        @NotNull UUID memberAUserId,
        @NotNull UUID memberBUserId,
        @NotBlank String relationshipTypeCode,
        LocalDate sinceDate
) {
}