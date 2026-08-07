package org.our_place.gallery.api;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.dto_shared.MediaSummaryShared;
import org.our_place.gallery.application.service.MediaQueryService;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.our_place.common.shared.r2.R2PresignedUrlGenerator;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GalleryApiImpl implements GalleryApi {

    private final MediaQueryService mediaQueryService;
    //TODO: Lo de abajo debe ser un simple servicio
    private final MediaRepository mediaRepository;
    private final R2PresignedUrlGenerator r2PresignedUrlGenerator;

    @Override
    public String getUrlById(UUID id) {
        String r2Key = mediaQueryService.getUrlById(id);
        return r2PresignedUrlGenerator.generateGetUrl(r2Key);
    }

    @Override
    public List<String> getUrlsByIdsBach(List<UUID> ids) {
        return mediaQueryService.getUrlsByIds(ids).stream()
                .map(r2PresignedUrlGenerator::generateGetUrl)
                .toList();
    }

    @Override
    public MediaSummaryShared getMediaById(UUID id) {
        var media = mediaQueryService.getMediaById(id);
        return new MediaSummaryShared(
                media.id(),
                r2PresignedUrlGenerator.generateGetUrl(media.thumbnailUrl()),
                media.mediaTypeCode(),
                media.takenAt()
        );
    }

    @Override
    public List<MediaSummaryShared> getMediasByIdsBach(List<UUID> ids) {
        return mediaQueryService.getMediasByIds(ids).stream()
                .map(media -> new MediaSummaryShared(
                        media.id(),
                        r2PresignedUrlGenerator.generateGetUrl(media.thumbnailUrl()),
                        media.mediaTypeCode(),
                        media.takenAt()
                )).toList();
    }

    @Override
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