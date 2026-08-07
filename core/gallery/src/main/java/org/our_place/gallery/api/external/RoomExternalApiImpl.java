package org.our_place.gallery.api.external;

import lombok.RequiredArgsConstructor;
import org.our_place.room.api.RoomApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomExternalApiImpl implements RoomExternalApi {

    private final RoomApi roomApi;

    @Override
    public UUID getRoomOwnerId(UUID roomId) {
        return roomApi.getRoomOwnerId(roomId);
    }

    @Override
    public boolean isMember(UUID roomId, UUID userId) {
        return roomApi.isMember(roomId, userId);
    }

    @Override
    public List<UUID> getActiveMemberIds(UUID roomId) {
        return roomApi.getActiveMemberIds(roomId);
    }
}
