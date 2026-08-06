package org.our_place.calendar.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

/** Se lanza cuando quien consulta el calendario no es miembro de la room. */
public class CalendarAccessForbiddenException extends ResultException {
    public CalendarAccessForbiddenException(UUID roomId, UUID userId) {
        super(
                "user not a member of room, roomId=" + roomId + ", userId=" + userId,
                new ResultIssue("CALENDAR_ACCESS_FORBIDDEN", "No tienes acceso al calendario de esta sala.", ResultIssue.Severity.WARNING)
        );
    }
}