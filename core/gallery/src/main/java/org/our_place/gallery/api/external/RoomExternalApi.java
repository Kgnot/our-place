package org.our_place.gallery.api.external;

import java.util.List;
import java.util.UUID;

public interface RoomExternalApi {

    UUID getRoomOwnerId(UUID roomId);

    boolean isMember(UUID roomId, UUID userId);

    List<UUID> getActiveMemberIds(UUID roomId);
}
