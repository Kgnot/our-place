package org.our_place.room.application.usecase.command;

import java.util.UUID;

public record InviteMemberCommand(
        UUID roomId,
        String invitedEmail,
        String roleCode,
        UUID invitedByUserId
) {
}