package org.our_place.calendar.service;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.external.GalleryExternalApi;
import org.our_place.calendar.api.external.MediaDto;
import org.our_place.calendar.domain.exception.ImportantDateNotFoundException;
import org.our_place.calendar.persistence.entity.ImportantDate;
import org.our_place.calendar.persistence.entity.ImportantDateMedia;
import org.our_place.calendar.persistence.repository.ImportantDateMediaRepository;
import org.our_place.calendar.persistence.repository.ImportantDateRepository;
import org.our_place.calendar.service.dto.ImportantDateDetailDto;
import org.our_place.calendar.service.dto.ImportantDateDto;
import org.our_place.calendar.service.dto.MediaServiceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImportantDateQueryService {

    private final ImportantDateRepository importantDateRepository;
    private final ImportantDateMediaRepository importantDateMediaRepository;
    private final GalleryExternalApi galleryExternalApi;

    public List<ImportantDateDto> listByRoom(UUID roomId) {
        List<ImportantDate> dates = importantDateRepository.findByRoomIdOrderByEventDateAsc(roomId);

        if (dates.isEmpty()) return List.of();

        // Batch: todos los mediaIds de todas las ImportantDate del room
        List<UUID> dateIds = dates.stream().map(ImportantDate::getId).toList();

        // Buscar todos los links de una vez
        List<ImportantDateMedia> allLinks = importantDateMediaRepository.findByIdImportantDateIdIn(dateIds);

        // Mapear importantDateId → [mediaId]
        Map<UUID, List<UUID>> mediaIdsByDateId = allLinks.stream()
                .collect(Collectors.groupingBy(
                        link -> link.getId().getImportantDateId(),
                        Collectors.mapping(link -> link.getId().getMediaId(), Collectors.toList())
                ));

        // Batch: todas las fotos de una sola llamada a Gallery
        List<UUID> allMediaIds = allLinks.stream()
                .map(link -> link.getId().getMediaId())
                .distinct()
                .toList();

        Map<UUID, MediaServiceDto> photosById = allMediaIds.isEmpty()
                ? Collections.emptyMap()
                : galleryExternalApi.getMediasByIdsBach(allMediaIds).stream()
                  .collect(Collectors.toMap(
                          MediaDto::id,
                          m -> new MediaServiceDto(m.id(), m.thumbnailUrl(), m.mediaTypeCode(), m.takenAt())
                  ));

        // Mapear
        return dates.stream()
                .map(d -> {
                    List<MediaServiceDto> photos = mediaIdsByDateId
                            .getOrDefault(d.getId(), List.of())
                            .stream()
                            .map(photosById::get)
                            .filter(Objects::nonNull)
                            .toList();

                    return new ImportantDateDto(
                            d.getId(), d.getType().getCode(), d.getType().getName(),
                            d.getTitle(), d.getEventDate(), d.isRecurring(), photos
                    );
                })
                .toList();
    }

    public ImportantDateDetailDto getDetail(UUID importantDateId) {
        ImportantDate date = importantDateRepository.findById(importantDateId)
                .orElseThrow(() -> new ImportantDateNotFoundException(importantDateId));

        List<UUID> mediaIds = importantDateMediaRepository
                .findByIdImportantDateId(importantDateId)
                .stream()
                .map(m -> m.getId().getMediaId())
                .toList();

        List<MediaDto> photos = mediaIds.isEmpty()
                ? List.of()
                : galleryExternalApi.getMediasByIdsBach(mediaIds);
        var photosMapped = photos.stream().map(mediaDto -> new MediaServiceDto(
                mediaDto.id(),
                mediaDto.thumbnailUrl(),
                mediaDto.mediaTypeCode(),
                mediaDto.takenAt()
        )).toList();


        return new ImportantDateDetailDto(
                date.getId(),
                date.getType().getCode(),
                date.getType().getName(),
                date.getTitle(),
                date.getEventDate(),
                date.isRecurring(),
                date.getNotifyDaysBefore(),
                photosMapped
        );
    }

}