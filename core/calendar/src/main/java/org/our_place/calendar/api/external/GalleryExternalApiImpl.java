package org.our_place.calendar.api.external;

import lombok.extern.slf4j.Slf4j;
import org.our_place.gallery.api.GalleryApi;
import org.our_place.gallery.api.dto_shared.UploadMediaCommandShared;
import org.our_place.gallery.api.dto_shared.UploadMediaOutputShared;
import org.our_place.gallery.application.service.dto.MediaSummaryDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class GalleryExternalApiImpl implements GalleryExternalApi {

    private final GalleryApi galleryApi;

    public GalleryExternalApiImpl(GalleryApi galleryApi) {
        this.galleryApi = galleryApi;
    }

    @Override
    public String getUrlById(UUID id) {
        return galleryApi.getUrlById(id);
    }

    @Override
    public List<String> getUrlsByIdsBach(List<UUID> ids) {
        return galleryApi.getUrlsByIdsBach(ids);
    }

    @Override
    public MediaDto getMediaById(UUID id) {
        var media = galleryApi.getMediaById(id);
        return new MediaDto(
                media.id(),
                media.thumbnailUrl(),
                media.mediaTypeCode(),
                media.takenAt()
        );
    }

    @Override
    public List<MediaDto> getMediasByIdsBach(List<UUID> ids) {
        var res = galleryApi.getMediasByIdsBach(ids)
                .stream()
                .map(media -> new MediaDto(
                        media.id(),
                        media.thumbnailUrl(),
                        media.mediaTypeCode(),
                        media.takenAt()
                )).toList();
        log.info("Resultado por baches: {}", res);
        return res;
    }

    @Override
    public List<MediaSummaryDto> getMediaByRoomAndDateRange(UUID roomId, LocalDate from, LocalDate to) {
        OffsetDateTime start = from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = to.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

        return galleryApi
                .findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(roomId, start, end)
                .stream()
                .map(mediaSummaryShared -> {
                    return new MediaSummaryDto(
                            mediaSummaryShared.id(),
                            mediaSummaryShared.thumbnailUrl(),
                            mediaSummaryShared.mediaTypeCode(),
                            mediaSummaryShared.takenAt()
                    );
                })
                .toList();
    }

    @Override
    public List<MediaSummaryDto> getMediaByRoomAndDate(UUID roomId, LocalDate date) {
        return getMediaByRoomAndDateRange(roomId, date, date);
    }

    @Override
    public UploadMediaOutputShared uploadMedia(UploadMediaCommandShared cmd) {
        return galleryApi.uploadMedia(cmd);
    }
}
