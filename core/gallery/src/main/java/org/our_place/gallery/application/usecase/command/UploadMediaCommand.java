package org.our_place.gallery.application.usecase.command;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UploadMediaCommand(
        UUID roomId,
        UUID uploadedByUserId,
        String r2Url,
        String mediaTypeCode,
        String mimeType,
        Long fileSizeBytes,
        OffsetDateTime takenAt,
        Double latitude,
        Double longitude,
        String caption
) {
}