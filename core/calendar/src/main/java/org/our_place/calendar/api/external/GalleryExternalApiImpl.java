package org.our_place.calendar.api.external;

import org.our_place.gallery.api.GalleryApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
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
                media.thumbnailUrl()
        );
    }

    @Override
    public List<MediaDto> getMediasByIdsBach(List<UUID> ids) {
        return galleryApi.getMediasByIdsBach(ids)
                .stream()
                .map(media -> new MediaDto(
                        media.id(),
                        media.thumbnailUrl()
                )).toList();
    }
}
