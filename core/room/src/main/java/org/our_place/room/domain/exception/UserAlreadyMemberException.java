package org.our_place.room.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class UserAlreadyMemberException extends ResultException {
    public UserAlreadyMemberException(UUID roomId, UUID userLoginId) {
        super(
                "user already a member, roomId=" + roomId + ", userLoginId=" + userLoginId,
                new ResultIssue("USER_ALREADY_MEMBER", "Ya perteneces a esta sala.", ResultIssue.Severity.WARNING)
        );
    }
}