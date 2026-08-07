package org.our_place.gallery.infra.controller.response;

import java.util.List;
import java.util.UUID;

public record BatchUploadResponse(
        List<UploadItem> items
) {
    public record UploadItem(
            UUID mediaId,
            String uploadUrl,   // presigned URL PUT a R2
            String r2Key        // key donde tiene que subir en R2
    ) {}
}