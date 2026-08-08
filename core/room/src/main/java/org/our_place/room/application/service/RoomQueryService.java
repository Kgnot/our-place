package org.our_place.room.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.room.application.service.dto.UserRoomDto;
import org.our_place.room.application.service.mapper.RoomMapper;
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

/**
 * Solo lectura — proyecta a DTO, no muta estado.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomQueryService {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;

    public RoomDto getRoomDetail(UUID roomId) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
        return RoomMapper.toDto(room);
    }

    public List<RoomMemberDto> listMembers(UUID roomId) {
        if (!roomsRepository.existsById(roomId)) {
            throw new RoomNotFoundException(roomId);
        }
        return roomMemberRepository.findByIdRoomId(roomId).stream()
                .map(RoomMapper::toDto)
                .toList();
    }

    public List<UserRoomDto> listRoomsForUser(UUID userLoginId) {
        return roomMemberRepository.findByIdUserLoginId(userLoginId).stream()
                .map(RoomMapper::toUserRoomDto)
                .toList();
    }

    public List<UserRoomDto> listActiveRoomsForUser(UUID userLoginId) {
        return roomMemberRepository.findByIdUserLoginIdAndStatus(userLoginId, "active").stream()
                .map(RoomMapper::toUserRoomDto)
                .toList();
    }

    public List<UserRoomDto> searchRooms(UUID userLoginId, String query) {
        if (query == null || query.isBlank()) {
            return listActiveRoomsForUser(userLoginId);
        }
        return roomMemberRepository.searchByUserAndQuery(userLoginId, "active", query.trim()).stream()
                .map(RoomMapper::toUserRoomDto)
                .toList();
    }

    public List<UserRoomDto> searchRoomsAllStatuses(UUID userLoginId, String query) {
        if (query == null || query.isBlank()) {
            return listRoomsForUser(userLoginId);
        }
        return roomMemberRepository.searchByUserAndQueryAllStatuses(userLoginId, query.trim()).stream()
                .map(RoomMapper::toUserRoomDto)
                .toList();
    }


}