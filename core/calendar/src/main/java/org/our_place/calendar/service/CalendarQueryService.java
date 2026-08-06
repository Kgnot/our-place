package org.our_place.calendar.service;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.external.GalleryExternalApi;
import org.our_place.calendar.api.external.MediaDto;
import org.our_place.calendar.domain.exception.DayEntryNotFoundException;
import org.our_place.calendar.persistence.entity.DayEntry;
import org.our_place.calendar.persistence.entity.DayEntryMedia;
import org.our_place.calendar.persistence.repository.DayEntryMediaRepository;
import org.our_place.calendar.persistence.repository.DayEntryRepository;
import org.our_place.calendar.service.dto.CalendarDayDto;
import org.our_place.calendar.service.dto.CalendarMonthDto;
import org.our_place.calendar.service.dto.DayEntryDetailDto;
import org.our_place.calendar.service.dto.MediaDetailDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarQueryService {

    private final DayEntryRepository dayEntryRepository;
    private final DayEntryMediaRepository dayEntryMediaRepository;
    private final GalleryExternalApi galleryExternalApi;

    public CalendarMonthDto getMonth(UUID roomId, int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        Map<LocalDate, DayEntry> entriesByDate = dayEntryRepository
                .findByIdRoomIdAndIdEntryDateBetween(roomId, firstDay, lastDay)
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getId().getEntryDate(),
                        Function.identity()
                ));

        List<DayEntryMedia> dayMedias = dayEntryMediaRepository
                .findByIdRoomIdAndIdEntryDateBetween(roomId, firstDay, lastDay);

        Map<LocalDate, List<UUID>> mediaIdsByDate = dayMedias.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getId().getEntryDate(),
                        Collectors.mapping(
                                m -> m.getId().getMediaId(),
                                Collectors.toList()
                        )
                ));

        List<UUID> allMediaIds = dayMedias.stream()
                .map(m -> m.getId().getMediaId())
                .distinct()
                .toList();

        Map<UUID, MediaDto> mediasById = allMediaIds.isEmpty()
                ? Collections.emptyMap()
                : galleryExternalApi.getMediasByIdsBach(allMediaIds)
                  .stream().collect(Collectors.toMap(MediaDto::id, Function.identity()));

        List<CalendarDayDto> days = new ArrayList<>(yearMonth.lengthOfMonth());

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {

            LocalDate date = yearMonth.atDay(day);
            DayEntry entry = entriesByDate.get(date);
            List<MediaDetailDto> medias = mediaIdsByDate
                    .getOrDefault(date, List.of())
                    .stream()
                    .map(mediasById::get)
                    .filter(Objects::nonNull)
                    .map(media -> new MediaDetailDto(
                            media.id(),
                            media.url()
                    ))
                    .toList();

            days.add(new CalendarDayDto(
                    date,
                    entry != null,
                    entry != null ? entry.getMoodEmoji() : null,
                    medias,
                    medias.size()
            ));
        }

        return new CalendarMonthDto(year, month, days);
    }

    public DayEntryDetailDto getDayDetail(UUID roomId, LocalDate date) {

        DayEntry entry = dayEntryRepository
                .findByIdRoomIdAndIdEntryDate(roomId, date)
                .orElseThrow(() ->
                        new DayEntryNotFoundException(roomId, date.toString()));

        List<UUID> mediaIds = dayEntryMediaRepository
                .findByIdRoomIdAndIdEntryDate(roomId, date)
                .stream()
                .map(m -> m.getId().getMediaId())
                .filter(Objects::nonNull)
                .toList();

        List<MediaDetailDto> medias = galleryExternalApi.getMediasByIdsBach(mediaIds)
                .stream()
                .map(media -> new MediaDetailDto(
                        media.id(),
                        media.url()
                ))
                .toList();

        return new DayEntryDetailDto(
                date,
                entry.getContent(),
                entry.getMoodEmoji(),
                medias
        );
    }
}
