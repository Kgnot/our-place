package org.our_place.gallery.infra.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.our_place.gallery.domain.vo.MediaType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ConfirmUploadRequest(
        @Valid @NotEmpty @Size(max = 50) List<ConfirmedItem> items
) {
    public record ConfirmedItem(
            @NotNull UUID mediaId,
            @NotNull String r2Key,
            @NotNull MediaType mediaTypeCode, // este es el código
            @NotNull String mimeType,
            Long fileSizeBytes,
            OffsetDateTime takenAt,
            Double latitude, // aqui un "punto para la localización"
            Double longitude,
            String caption
    ) {}
}
