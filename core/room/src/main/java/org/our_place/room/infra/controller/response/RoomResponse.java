package org.our_place.room.infra.controller.response;

import org.our_place.room.application.service.dto.RoomDto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String name,
        String statusCode,
        String relationshipTypeCode,
        UUID ownerUserId,
        LocalDate anniversaryDate,
        String timezone,
        OffsetDateTime createdAt
) {
    public static RoomResponse from(RoomDto dto) {
        return new RoomResponse(
                dto.id(), dto.name(), dto.statusCode(), dto.relationshipTypeCode(),
                dto.ownerUserId(), dto.anniversaryDate(), dto.timezone(), dto.createdAt()
        );
    }
}