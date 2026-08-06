package org.our_place.room.application.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RoomMemberDto(
        UUID userLoginId,
        String roleCode,
        String status,
        String nickname,
        OffsetDateTime joinedAt
) {
}