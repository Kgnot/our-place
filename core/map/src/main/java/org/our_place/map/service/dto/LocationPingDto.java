package org.our_place.map.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LocationPingDto(
        UUID userLoginId,
        String locationWkt,
        Short batteryLevel,
        OffsetDateTime recordedAt
) {}