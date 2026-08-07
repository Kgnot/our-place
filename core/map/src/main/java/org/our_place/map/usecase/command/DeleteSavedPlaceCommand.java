package org.our_place.map.usecase.command;

import java.util.UUID;

public record DeleteSavedPlaceCommand(
        UUID roomId,
        UUID savedPlaceId
) {}