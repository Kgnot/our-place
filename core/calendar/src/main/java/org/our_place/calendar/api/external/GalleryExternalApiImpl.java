package org.our_place.calendar.api.external;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GalleryExternalApiImpl implements GalleryExternalApi {
    @Override
    public String getUrlById(UUID id) {
        return "";
    }

    @Override
    public List<String> getUrlsByIdsBach(List<UUID> ids) {
        return List.of();
    }

    @Override
    public MediaDto getMediaById(UUID id) {
        return null;
    }

    @Override
    public List<MediaDto> getMediasByIdsBach(List<UUID> ids) {
        return List.of();
    }
}
