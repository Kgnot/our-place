package org.our_place.gallery.api;

import org.our_place.common.shared.SharedApi;
import org.our_place.gallery.service.dto.MediaDto;

import java.util.List;
import java.util.UUID;

@SharedApi(description = "API for managing gallery items")
public interface GalleryApi {

    String getUrlById(UUID id);

    List<String> getUrlsByIdsBach(List<UUID> ids);

    MediaDto getMediaById(UUID id);

    List<MediaDto> getMediasByIdsBach(List<UUID> ids);
}
