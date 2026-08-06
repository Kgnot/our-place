package org.our_place.room.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class InvitationNotFoundException extends ResultException {
    public InvitationNotFoundException(String token) {
        super(
                "invitation not found for token",
                new ResultIssue("INVITATION_NOT_FOUND", "Esta invitación no existe o ya no es válida.", ResultIssue.Severity.WARNING)
        );
    }
}