package org.our_place.room.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

/** Se lanza cuando quien ejecuta la acción no es owner de la room y la acción lo requiere (ej. invitar). */
public class RoomActionForbiddenException extends ResultException {
    public RoomActionForbiddenException(UUID roomId, UUID userId) {
        super(
                "user not allowed to perform this action, roomId=" + roomId + ", userId=" + userId,
                new ResultIssue("ROOM_ACTION_FORBIDDEN", "No tienes permiso para hacer esto en la sala.", ResultIssue.Severity.WARNING)
        );
    }
}