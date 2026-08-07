package org.our_place.gallery.api;

import org.our_place.common.shared.SharedApi;
import org.our_place.gallery.api.dto_shared.MediaSummaryShared;
import org.our_place.gallery.application.service.dto.MediaSummaryDto;

import java.util.List;
import java.util.UUID;

@SharedApi(description = "API for managing gallery items")
public interface GalleryApi {

    String getUrlById(UUID id);

    List<String> getUrlsByIdsBach(List<UUID> ids);

    MediaSummaryShared getMediaById(UUID id);

    List<MediaSummaryShared> getMediasByIdsBach(List<UUID> ids);
}
