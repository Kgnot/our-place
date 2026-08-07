package org.our_place.gallery.application.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaDetailDto(
        UUID id,
        String r2Url,
        String thumbnailUrl,
        String mediaTypeCode,
        String caption,
        OffsetDateTime takenAt,
        UUID uploadedByUserId,
        long commentCount,
        long reactionCount,
        String currentUserReactionType
) {
}