package org.our_place.room.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class RoomNotFoundException extends ResultException {
    public RoomNotFoundException(UUID roomId) {
        super(
                "room not found, id=" + roomId,
                new ResultIssue("ROOM_NOT_FOUND", "No encontramos esa sala.", ResultIssue.Severity.WARNING)
        );
    }
}