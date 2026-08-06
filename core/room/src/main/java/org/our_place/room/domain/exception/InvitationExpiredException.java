package org.our_place.room.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class InvitationExpiredException extends ResultException {
    public InvitationExpiredException(Long invitationId) {
        super(
                "invitation expired, id=" + invitationId,
                new ResultIssue("INVITATION_EXPIRED", "Esta invitación ya expiró.", ResultIssue.Severity.WARNING)
        );
    }
}