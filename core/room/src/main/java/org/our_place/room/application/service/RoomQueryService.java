package org.our_place.room.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.room.domain.exception.RoomNotFoundException;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.infra.persistence.repository.RoomsRepository;
import org.our_place.room.application.service.dto.RoomDto;
import org.our_place.room.application.service.dto.RoomMemberDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/** Solo lectura — proyecta a DTO, no muta estado (ver §2). */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomQueryService {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;

    public RoomDto getRoomDetail(UUID roomId) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
        return toDto(room);
    }

    public List<RoomMemberDto> listMembers(UUID roomId) {
        if (!roomsRepository.existsById(roomId)) {
            throw new RoomNotFoundException(roomId);
        }
        return roomMemberRepository.findByIdRoomId(roomId).stream()
                .map(this::toDto)
                .toList();
    }

    private RoomDto toDto(Rooms room) {
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

    private RoomMemberDto toDto(RoomMember member) {
        return new RoomMemberDto(
                member.getId().getUserLoginId(),
                member.getRoleCode(),
                member.getStatus(),
                member.getNickname(),
                member.getJoinedAt()
        );
    }
}