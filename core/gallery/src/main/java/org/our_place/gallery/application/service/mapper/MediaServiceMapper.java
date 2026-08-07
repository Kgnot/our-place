package org.our_place.gallery.application.service.mapper;

import org.our_place.gallery.application.service.dto.MediaCommentDto;
import org.our_place.gallery.application.service.dto.MediaSummaryDto;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.entity.MediaComment;

public class MediaServiceMapper {

    static public MediaSummaryDto toSummaryDto(Media media) {
        return new MediaSummaryDto(
                media.getId(), media.getThumbnailUrl(), media.getMediaType().getCode(), media.getTakenAt()
        );
    }

    static public MediaCommentDto toCommentDto(MediaComment comment) {
        return new MediaCommentDto(
                comment.getId(), comment.getUserLoginId(), comment.getContent(), comment.getCreatedAt()
        );
    }
}
