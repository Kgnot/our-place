package org.our_place.room.application.usecase.command;

import java.time.LocalDate;
import java.util.UUID;

public record SetMemberRelationshipCommand(
        UUID roomId,
        UUID memberAUserId,
        UUID memberBUserId,
        String relationshipTypeCode,
        LocalDate sinceDate
) {
}