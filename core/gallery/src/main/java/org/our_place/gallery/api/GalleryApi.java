package org.our_place.gallery.api;

import org.our_place.common.shared.SharedApi;
import org.our_place.gallery.api.dto_shared.MediaSummaryShared;
import org.our_place.gallery.api.dto_shared.UploadMediaCommandShared;
import org.our_place.gallery.api.dto_shared.UploadMediaOutputShared;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@SharedApi(description = "API for managing gallery items")
public interface GalleryApi {

    String getUrlById(UUID id);

    List<String> getUrlsByIdsBach(List<UUID> ids);

    MediaSummaryShared getMediaById(UUID id);

    List<MediaSummaryShared> getMediasByIdsBach(List<UUID> ids);

    List<MediaSummaryShared> findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(UUID roomId, OffsetDateTime start, OffsetDateTime end);

    UploadMediaOutputShared uploadMedia(UploadMediaCommandShared cmd);
}
