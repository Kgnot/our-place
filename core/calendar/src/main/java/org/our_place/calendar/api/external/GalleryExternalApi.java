package org.our_place.calendar.api.external;

//todo, cabiar el dto del service, a mappear aqui otro

import org.our_place.gallery.api.dto_shared.UploadMediaCommandShared;
import org.our_place.gallery.api.dto_shared.UploadMediaOutputShared;
import org.our_place.gallery.application.service.dto.MediaSummaryDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GalleryExternalApi {

    String getUrlById(UUID id);

    List<String> getUrlsByIdsBach(List<UUID> ids);

    MediaDto getMediaById(UUID id);

    List<MediaDto> getMediasByIdsBach(List<UUID> ids);

    List<MediaSummaryDto> getMediaByRoomAndDateRange(UUID roomId, LocalDate from, LocalDate to);

    List<MediaSummaryDto> getMediaByRoomAndDate(UUID roomId, LocalDate date);

    UploadMediaOutputShared uploadMedia(UploadMediaCommandShared cmd);

}

