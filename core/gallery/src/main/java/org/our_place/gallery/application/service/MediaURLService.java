package org.our_place.gallery.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.common.shared.r2.R2PresignedUrlGenerator;
import org.our_place.gallery.api.dto_shared.MediaSummaryShared;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaURLService {

    private final MediaRepository mediaRepository;
    private final R2PresignedUrlGenerator r2PresignedUrlGenerator;
    private final MediaQueryService mediaQueryService;


    public List<MediaSummaryShared> getMediasByIdsBach(List<UUID> ids) {
        return mediaQueryService.getMediasByIds(ids).stream()
                .map(media -> new MediaSummaryShared(
                        media.id(),
                        r2PresignedUrlGenerator.generateGetUrl(media.thumbnailUrl()),
                        media.mediaTypeCode(),
                        media.takenAt()
                )).toList();
    }

    public List<MediaSummaryShared> findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(
            UUID roomId, OffsetDateTime start, OffsetDateTime end) {
        return mediaRepository
                .findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(roomId, start, end)
                .stream()
                .map(media -> new MediaSummaryShared(
                        media.getId(),
                        r2PresignedUrlGenerator.generateGetUrl(media.getThumbnailUrl()),
                        media.getMediaType().getCode(),
                        media.getTakenAt()
                )).toList();
    }
}
