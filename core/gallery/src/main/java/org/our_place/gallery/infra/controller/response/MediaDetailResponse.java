package org.our_place.gallery.infra.controller.response;

import org.our_place.gallery.application.service.dto.MediaDetailDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaDetailResponse(
        UUID id,
        String r2Url,
        String thumbnailUrl,
        String mediaTypeCode,
        String caption,
        OffsetDateTime takenAt,
        UUID uploadedByUserId,
        long commentCount,
        long reactionCount,
        String currentUserReactionType
) {
    public static MediaDetailResponse from(MediaDetailDto dto) {
        return new MediaDetailResponse(
                dto.id(), dto.r2Url(), dto.thumbnailUrl(), dto.mediaTypeCode(), dto.caption(),
                dto.takenAt(), dto.uploadedByUserId(), dto.commentCount(), dto.reactionCount(),
                dto.currentUserReactionType()
        );
    }
}