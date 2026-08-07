package org.our_place.calendar.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record CreateImportantDateRequest(
        @NotNull String typeCode,
        @NotBlank String title,
        @NotNull LocalDate eventDate,
        boolean isRecurring,
        short notifyDaysBefore,
        @Valid @Size(max = 10) List<MediaItem> media
) {
    public record MediaItem(
            @NotBlank String r2Key,
            @NotBlank String mediaTypeCode,
            @NotBlank String mimeType,
            Long fileSizeBytes,
            OffsetDateTime takenAt,
            Double latitude,
            Double longitude,
            String caption
    ) {
    }
}