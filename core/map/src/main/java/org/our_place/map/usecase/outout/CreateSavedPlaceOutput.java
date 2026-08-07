package org.our_place.map.usecase.outout;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateSavedPlaceOutput(
        UUID id,
        UUID roomId,
        String categoryCode,
        String name,
        OffsetDateTime createdAt
) {
}