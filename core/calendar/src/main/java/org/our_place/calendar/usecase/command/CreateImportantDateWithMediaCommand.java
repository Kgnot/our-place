package org.our_place.calendar.usecase.command;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CreateImportantDateWithMediaCommand(
        UUID roomId,
        String typeCode,
        String title,
        LocalDate eventDate,
        boolean isRecurring,
        short notifyDaysBefore,
        UUID createdByUserId,
        List<MediaItem> mediaItems
) {
    public record MediaItem(
            String r2Key,
            String mediaTypeCode,
            String mimeType,
            Long fileSizeBytes,
            OffsetDateTime takenAt,
            Double latitude,
            Double longitude,
            String caption
    ) {}
}