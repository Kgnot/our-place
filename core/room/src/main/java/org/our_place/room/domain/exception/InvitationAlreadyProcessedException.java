package org.our_place.room.domain.exception;


import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class InvitationAlreadyProcessedException extends ResultException {
    public InvitationAlreadyProcessedException(Long invitationId) {
        super(
                "invitation already processed, id=" + invitationId,
                new ResultIssue("INVITATION_ALREADY_PROCESSED", "Esta invitación ya fue utilizada.", ResultIssue.Severity.WARNING)
        );
    }
}