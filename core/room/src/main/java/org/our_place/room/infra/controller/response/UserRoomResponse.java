package org.our_place.room.infra.controller.response;

import org.our_place.room.application.service.dto.UserRoomDto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserRoomResponse(
        UUID roomId,
        String roomName,
        String statusCode,
        String relationshipTypeCode,
        UUID ownerUserId,
        LocalDate anniversaryDate,
        String timezone,
        OffsetDateTime roomCreatedAt,
        String roleCode,
        String memberStatus,
        String nickname,
        OffsetDateTime joinedAt
) {
    public static UserRoomResponse from(UserRoomDto dto) {
        return new UserRoomResponse(
                dto.roomId(),
                dto.roomName(),
                dto.statusCode(),
                dto.relationshipTypeCode(),
                dto.ownerUserId(),
                dto.anniversaryDate(),
                dto.timezone(),
                dto.roomCreatedAt(),
                dto.roleCode(),
                dto.memberStatus(),
                dto.nickname(),
                dto.joinedAt()
        );
    }
}