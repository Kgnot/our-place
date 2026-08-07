package org.our_place.gallery.api;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.dto_shared.MediaSummaryShared;
import org.our_place.gallery.api.dto_shared.UploadMediaCommandShared;
import org.our_place.gallery.api.dto_shared.UploadMediaOutputShared;
import org.our_place.gallery.application.service.MediaQueryService;
import org.our_place.gallery.application.service.MediaURLService;
import org.our_place.gallery.application.usecase.UploadMediaUseCase;
import org.our_place.gallery.application.usecase.command.UploadMediaCommand;
import org.our_place.common.shared.r2.R2PresignedUrlGenerator;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GalleryApiImpl implements GalleryApi {

    private final MediaQueryService mediaQueryService;
    private final MediaURLService mediaURLService;
    private final R2PresignedUrlGenerator r2PresignedUrlGenerator;
    private final UploadMediaUseCase uploadMediaUseCase;

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
        return mediaURLService.findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(roomId, start, end);
    }

    @Override
    public UploadMediaOutputShared uploadMedia(UploadMediaCommandShared cmd) {
        var res = uploadMediaUseCase.execute(new UploadMediaCommand(
                cmd.roomId(),
                cmd.uploadedByUserId(),
                cmd.r2Url(),
                cmd.mediaTypeCode(),
                cmd.mimeType(),
                cmd.fileSizeBytes(),
                cmd.takenAt(),
                cmd.latitude(),
                cmd.longitude(),
                cmd.caption()
        ));
        return new UploadMediaOutputShared(res.mediaId());
    }
}