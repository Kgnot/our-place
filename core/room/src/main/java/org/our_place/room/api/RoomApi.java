package org.our_place.room.api;

import org.our_place.common.shared.SharedApi;

import java.util.List;
import java.util.UUID;

@SharedApi(description = "API interna del módulo room, consumible in-process por otros módulos " +
        "(ej. affection o pet necesitan saber si un usuario pertenece a una room, o quién es el owner)")
public interface RoomApi {

    boolean isMember(UUID roomId, UUID userId);

    UUID getRoomOwnerId(UUID roomId);

    List<UUID> getActiveMemberIds(UUID roomId);
}