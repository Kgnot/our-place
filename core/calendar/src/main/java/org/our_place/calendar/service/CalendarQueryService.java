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
import org.our_place.gallery.application.service.dto.MediaSummaryDto;
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
    private final GalleryExternalApi galleryExternalApi;

    public CalendarMonthDto getMonth(UUID roomId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        // 1. Entries del mes (diario/mood)
        Map<LocalDate, DayEntry> entriesByDate = dayEntryRepository
                .findByIdRoomIdAndIdEntryDateBetween(roomId, firstDay, lastDay)
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getId().getEntryDate(),
                        Function.identity()
                ));

        // 2. Fotos del mes — query directa a gallery por takenAt
        Map<LocalDate, List<MediaSummaryDto>> photosByDate = galleryExternalApi
                .getMediaByRoomAndDateRange(roomId, firstDay, lastDay)
                .stream()
                .collect(Collectors.groupingBy(
                        media -> media.takenAt().toLocalDate()
                ));

        // 3. Construir cada día
        List<CalendarDayDto> days = new ArrayList<>(yearMonth.lengthOfMonth());

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DayEntry entry = entriesByDate.get(date);
            List<MediaSummaryDto> photos = photosByDate.getOrDefault(date, List.of());

            days.add(new CalendarDayDto(
                    date,
                    entry != null,
                    entry != null ? entry.getMoodEmoji() : null,
                    !photos.isEmpty(),
                    photos.size(),
                    photos.stream().limit(4).toList()   // preview: 4 thumbnails
            ));
        }

        return new CalendarMonthDto(year, month, days);
    }

    public DayEntryDetailDto getDayDetail(UUID roomId, LocalDate date) {
        // Entry (puede no existir si solo hay fotos)
        DayEntry entry = dayEntryRepository
                .findByIdRoomIdAndIdEntryDate(roomId, date)
                .orElse(null);

        // Fotos de ese día
        List<MediaSummaryDto> photos = galleryExternalApi
                .getMediaByRoomAndDate(roomId, date);

        return new DayEntryDetailDto(
                date,
                entry != null ? entry.getContent() : null,
                entry != null ? entry.getMoodEmoji() : null,
                photos
        );
    }
}