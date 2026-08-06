package org.our_place.calendar.api.external;

import java.util.List;
import java.util.UUID;

public interface GalleryExternalApi {

    String getUrlById(UUID id);

    List<String> getUrlsByIdsBach(List<UUID> ids);

    MediaDto getMediaById(UUID id);

    List<MediaDto> getMediasByIdsBach(List<UUID> ids);
}

