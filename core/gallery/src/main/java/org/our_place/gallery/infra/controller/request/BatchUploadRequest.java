package org.our_place.gallery.infra.controller.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.our_place.gallery.domain.vo.MediaType;

import java.time.OffsetDateTime;
import java.util.List;

public record BatchUploadRequest(
        @Valid @Size(min=1,max = 50)List<Entry> entries
        ) {
    public record Entry(
            @NotNull MediaType mediaTypeCode,
            @NotBlank String mimeType,
            Long fileSizeBytes,
            OffsetDateTime takenAt,
            String caption

    ){}
}
