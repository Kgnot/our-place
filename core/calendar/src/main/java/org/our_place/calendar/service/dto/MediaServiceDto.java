package org.our_place.calendar.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaServiceDto(
        UUID id,
        String thumbnailUrl,
        String mediaTypeCode,
        OffsetDateTime takenAt
) {
}
