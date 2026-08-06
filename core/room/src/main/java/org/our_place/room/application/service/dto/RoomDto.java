package org.our_place.room.application.service.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RoomDto(
        UUID id,
        String name,
        String statusCode,
        String relationshipTypeCode,
        UUID ownerUserId,
        LocalDate anniversaryDate,
        String timezone,
        OffsetDateTime createdAt
) {
}