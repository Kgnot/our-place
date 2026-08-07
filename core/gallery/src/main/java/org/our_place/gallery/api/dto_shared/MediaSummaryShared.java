package org.our_place.gallery.api.dto_shared;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaSummaryShared(
        UUID id,
        String thumbnailUrl,
        String mediaTypeCode,
        OffsetDateTime takenAt
) {
}