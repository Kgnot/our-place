package org.our_place.map.usecase.command;

import java.util.UUID;

public record RecordLocationPingCommand(
        UUID roomId,
        UUID userLoginId,
        String locationWkt,
        Short batteryLevel
) {}