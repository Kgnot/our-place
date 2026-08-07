package org.our_place.gallery.infra.controller.response;

import org.our_place.gallery.application.service.dto.MediaCommentDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaCommentResponse(
        UUID id,
        UUID userLoginId,
        String content,
        OffsetDateTime createdAt
) {
    public static MediaCommentResponse from(MediaCommentDto dto) {
        return new MediaCommentResponse(dto.id(), dto.userLoginId(), dto.content(), dto.createdAt());
    }
}