package org.our_place.gallery.application.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaCommentDto(
        UUID id,
        UUID userLoginId,
        String content,
        OffsetDateTime createdAt
) {
}