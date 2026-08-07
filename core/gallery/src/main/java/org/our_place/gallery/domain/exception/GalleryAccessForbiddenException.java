package org.our_place.gallery.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

/**
 * Se lanza cuando quien consulta/sube contenido no es miembro de la room.
 */
public class GalleryAccessForbiddenException extends ResultException {
    public GalleryAccessForbiddenException(UUID roomId, UUID userId) {
        super(
                "user not a member of room, roomId=" + roomId + ", userId=" + userId,
                new ResultIssue("GALLERY_ACCESS_FORBIDDEN", "No tienes acceso a la galería de esta sala.", ResultIssue.Severity.WARNING)
        );
    }
}