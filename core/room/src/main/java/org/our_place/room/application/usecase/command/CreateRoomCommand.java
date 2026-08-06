package org.our_place.room.application.usecase.command;

import java.time.LocalDate;
import java.util.UUID;

public record CreateRoomCommand(
        String name,
        String relationshipTypeCode,
        LocalDate anniversaryDate,
        String timezone,
        UUID ownerUserId
) {
}