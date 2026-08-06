package org.our_place.room.application.usecase.output;

import java.util.UUID;

public record AcceptInvitationOutput(UUID roomId, String roleCode) {
}