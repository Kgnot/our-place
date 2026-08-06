package org.our_place.room.api;

import lombok.RequiredArgsConstructor;
import org.our_place.room.domain.exception.RoomNotFoundException;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.infra.persistence.repository.RoomsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomApiImpl implements RoomApi {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Override
    public boolean isMember(UUID roomId, UUID userId) {
        return roomMemberRepository.existsByIdRoomIdAndIdUserLoginId(roomId, userId);
    }

    @Override
    public UUID getRoomOwnerId(UUID roomId) {
        return roomsRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId))
                .getOwnerUserId();
    }

    @Override
    public List<UUID> getActiveMemberIds(UUID roomId) {
        return roomMemberRepository.findByIdRoomId(roomId).stream()
                .filter(m -> "active".equals(m.getStatus()))
                .map(m -> m.getId().getUserLoginId())
                .toList();
    }
}