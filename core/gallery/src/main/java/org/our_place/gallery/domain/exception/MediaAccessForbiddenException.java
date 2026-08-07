package org.our_place.gallery.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

/** Se lanza cuando quien intenta borrar/editar la foto no es ni el uploader ni el owner de la room. */
public class MediaAccessForbiddenException extends ResultException {
    public MediaAccessForbiddenException(UUID mediaId, UUID userId) {
        super(
                "user not allowed to modify this media, mediaId=" + mediaId + ", userId=" + userId,
                new ResultIssue("MEDIA_ACCESS_FORBIDDEN", "No tienes permiso para modificar esta foto.", ResultIssue.Severity.WARNING)
        );
    }
}