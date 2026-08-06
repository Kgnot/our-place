package org.our_place.room.infra.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateRoomRequest(
        @NotBlank String name,
        String relationshipTypeCode,
        LocalDate anniversaryDate,
        String timezone
) {
}