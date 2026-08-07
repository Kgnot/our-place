package org.our_place.gallery.api;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.dto_shared.MediaSummaryShared;
import org.our_place.gallery.application.service.MediaQueryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GalleryApiImpl implements GalleryApi {

    private final MediaQueryService mediaQueryService;

    @Override
    public String getUrlById(UUID id) {
        return mediaQueryService.getUrlById(id);
    }

    @Override
    public List<String> getUrlsByIdsBach(List<UUID> ids) {
        return mediaQueryService.getUrlsByIds(ids);
    }

    @Override
    public MediaSummaryShared getMediaById(UUID id) {
        var media = mediaQueryService.getMediaById(id);
        return new MediaSummaryShared(
                media.id(),
                media.thumbnailUrl(),
                media.mediaTypeCode(),
                media.takenAt()
        );
    }

    @Override
    public List<MediaSummaryShared> getMediasByIdsBach(List<UUID> ids) {
        return mediaQueryService.getMediasByIds(ids)
                .stream()
                .map(media -> new MediaSummaryShared(
                        media.id(),
                        media.thumbnailUrl(),
                        media.mediaTypeCode(),
                        media.takenAt()
                )).toList();
    }
}
