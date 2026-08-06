package org.our_place.room.infra.controller.response;

import org.our_place.room.application.service.dto.RoomMemberDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RoomMemberResponse(
        UUID userLoginId,
        String roleCode,
        String status,
        String nickname,
        OffsetDateTime joinedAt
) {
    public static RoomMemberResponse from(RoomMemberDto dto) {
        return new RoomMemberResponse(
                dto.userLoginId(), dto.roleCode(), dto.status(), dto.nickname(), dto.joinedAt()
        );
    }
}