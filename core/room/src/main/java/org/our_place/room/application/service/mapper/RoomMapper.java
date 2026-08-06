package org.our_place.room.application.service.mapper;

import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.application.service.dto.RoomDto;
import org.our_place.room.application.service.dto.RoomMemberDto;
import org.our_place.room.application.service.dto.UserRoomDto;

public final class RoomMapper {

    private RoomMapper() {}

    public static RoomDto toDto(Rooms room) {
        return new RoomDto(
                room.getId(),
                room.getName(),
                room.getStatus().getCode(),
                room.getRelationshipType() != null ? room.getRelationshipType().getCode() : null,
                room.getOwnerUserId(),
                room.getAnniversaryDate(),
                room.getTimezone(),
                room.getCreatedAt()
        );
    }

    public static RoomMemberDto toDto(RoomMember member) {
        return new RoomMemberDto(
                member.getId().getUserLoginId(),
                member.getRoleCode(),
                member.getStatus(),
                member.getNickname(),
                member.getJoinedAt()
        );
    }

    public static UserRoomDto toUserRoomDto(RoomMember member) {
        Rooms room = member.getRoom();
        return new UserRoomDto(
                room.getId(),
                room.getName(),
                room.getStatus().getCode(),
                room.getRelationshipType() != null ? room.getRelationshipType().getCode() : null,
                room.getOwnerUserId(),
                room.getAnniversaryDate(),
                room.getTimezone(),
                room.getCreatedAt(),
                member.getRoleCode(),
                member.getStatus(),
                member.getNickname(),
                member.getJoinedAt()
        );
    }
}