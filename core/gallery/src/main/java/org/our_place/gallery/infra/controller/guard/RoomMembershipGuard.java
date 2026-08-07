package org.our_place.gallery.infra.controller.guard;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.external.RoomExternalApi;
import org.our_place.gallery.domain.exception.GalleryAccessForbiddenException;
import org.our_place.identity.api.SecurityContextApi;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Valida membresía de sala contra RoomApi  antes de cualquier operación de lectura o
 * escritura sobre media. Compartido por todos los controllers del módulo de media para no
 * duplicar la lógica de autorización.
 */
@Component
@RequiredArgsConstructor
public class RoomMembershipGuard {

    private final SecurityContextApi securityContextApi;
    private final RoomExternalApi roomApi;

    public UUID requireMember(UUID roomId) {
        UUID userId = securityContextApi.getCurrentUserId();
        if (!roomApi.isMember(roomId, userId)) {
            throw new GalleryAccessForbiddenException(roomId, userId);
        }
        return userId;
    }
}

