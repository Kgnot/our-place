package org.our_place.gallery.infra.controller.response;

import org.our_place.gallery.application.service.dto.MediaSummaryDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaSummaryResponse(
        UUID id,
        String thumbnailUrl,
        String mediaTypeCode,
        OffsetDateTime takenAt
) {
    public static MediaSummaryResponse from(MediaSummaryDto dto) {
        return new MediaSummaryResponse(dto.id(), dto.thumbnailUrl(), dto.mediaTypeCode(), dto.takenAt());
    }
}