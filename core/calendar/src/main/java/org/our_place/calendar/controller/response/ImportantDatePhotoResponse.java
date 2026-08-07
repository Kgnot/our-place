package org.our_place.calendar.controller.response;

import org.our_place.calendar.service.dto.MediaServiceDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ImportantDatePhotoResponse(
        UUID id,
        String thumbnailUrl,
        String mediaTypeCode,
        OffsetDateTime takenAt
) {
    public static ImportantDatePhotoResponse from(MediaServiceDto media) {
        return new ImportantDatePhotoResponse(
                media.id(),
                media.thumbnailUrl(),
                media.mediaTypeCode(),
                media.takenAt()
        );
    }
}