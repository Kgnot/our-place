package org.our_place.gallery.api.dto_shared;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UploadMediaCommandShared(
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
