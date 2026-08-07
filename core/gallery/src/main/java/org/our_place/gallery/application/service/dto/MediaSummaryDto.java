package org.our_place.gallery.application.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaSummaryDto(
        UUID id,
        String thumbnailUrl,
        String mediaTypeCode,
        OffsetDateTime takenAt
) {
    public MediaSummaryDto withThumbnailUrl(String newThumbnailUrl) {
        return new MediaSummaryDto(id, newThumbnailUrl, mediaTypeCode, takenAt);
    }
}