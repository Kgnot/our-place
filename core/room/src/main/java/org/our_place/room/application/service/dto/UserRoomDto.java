package org.our_place.room.application.service.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserRoomDto(
        UUID roomId,
        String roomName,
        String statusCode,
        String relationshipTypeCode,
        UUID ownerUserId,
        LocalDate anniversaryDate,
        String timezone,
        OffsetDateTime roomCreatedAt,
        // --- Info como miembro ---
        String roleCode,
        String memberStatus,
        String nickname,
        OffsetDateTime joinedAt
) {
}
