package org.our_place.map.controller.response;


import org.our_place.map.service.dto.LocationPingDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LocationPingResponse(
        UUID userLoginId,
        String locationWkt,
        Short batteryLevel,
        OffsetDateTime recordedAt
) {
    public static LocationPingResponse from(LocationPingDto dto) {
        return new LocationPingResponse(
                dto.userLoginId(),
                dto.locationWkt(),
                dto.batteryLevel(),
                dto.recordedAt()
        );
    }
}