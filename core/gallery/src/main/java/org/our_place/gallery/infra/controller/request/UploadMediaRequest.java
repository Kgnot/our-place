package org.our_place.gallery.infra.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record UploadMediaRequest(
        @NotBlank String r2Url,
        @NotBlank String mediaTypeCode,
        String mimeType,
        Long fileSizeBytes,
        OffsetDateTime takenAt,
        String caption
) {
}