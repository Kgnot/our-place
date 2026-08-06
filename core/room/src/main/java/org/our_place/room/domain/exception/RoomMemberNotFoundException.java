package org.our_place.room.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class RoomMemberNotFoundException extends ResultException {
    public RoomMemberNotFoundException(UUID roomId, UUID userLoginId) {
        super(
                "room member not found, roomId=" + roomId + ", userLoginId=" + userLoginId,
                new ResultIssue("ROOM_MEMBER_NOT_FOUND", "No eres miembro de esa sala.", ResultIssue.Severity.WARNING)
        );
    }
}