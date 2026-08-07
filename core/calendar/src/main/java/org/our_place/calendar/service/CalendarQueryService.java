package org.our_place.calendar.service;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.external.GalleryExternalApi;
import org.our_place.calendar.persistence.entity.DayEntry;
import org.our_place.calendar.persistence.entity.ImportantDate;
import org.our_place.calendar.persistence.repository.DayEntryRepository;
import org.our_place.calendar.persistence.repository.ImportantDateRepository;
import org.our_place.calendar.service.dto.*;
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
    private final ImportantDateRepository importantDateRepository;
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

        // 2. Fotos del mes
        Map<LocalDate, List<MediaSummaryDto>> photosByDate = galleryExternalApi
                .getMediaByRoomAndDateRange(roomId, firstDay, lastDay)
                .stream()
                .collect(Collectors.groupingBy(
                        media -> media.takenAt().toLocalDate()
                ));

        // 3. Fechas importantes del room → mapear a días del mes
        Map<LocalDate, List<ImportantDateInCalendarDto>> importantDatesByDate =
                resolveImportantDatesForMonth(roomId, year, month);

        // 4. Construir cada día
        List<CalendarDayDto> days = new ArrayList<>(yearMonth.lengthOfMonth());

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DayEntry entry = entriesByDate.get(date);
            List<MediaSummaryDto> photos = photosByDate.getOrDefault(date, List.of());
            List<ImportantDateInCalendarDto> impDates = importantDatesByDate.getOrDefault(date, List.of());

            days.add(new CalendarDayDto(
                    date,
                    entry != null,
                    entry != null ? entry.getMoodEmoji() : null,
                    !photos.isEmpty(),
                    photos.size(),
                    photos.stream().limit(4).toList(),
                    impDates
            ));
        }

        return new CalendarMonthDto(year, month, days);
    }

    public DayEntryDetailDto getDayDetail(UUID roomId, LocalDate date) {
        DayEntry entry = dayEntryRepository
                .findByIdRoomIdAndIdEntryDate(roomId, date)
                .orElse(null);

        List<MediaSummaryDto> photos = galleryExternalApi
                .getMediaByRoomAndDate(roomId, date);

        // Fechas importantes de ese día exacto
        List<ImportantDateInCalendarDto> impDates =
                resolveImportantDatesForDay(roomId, date);

        return new DayEntryDetailDto(
                date,
                entry != null ? entry.getContent() : null,
                entry != null ? entry.getMoodEmoji() : null,
                photos,
                impDates
        );
    }

    /**
     * Resuelve fechas importantes para un mes entero.
     * - Recurrentes: si month/day coincide con algún día del mes → aparece
     * - No recurrentes: si eventDate cae en este mes/año → aparece
     */
    private Map<LocalDate, List<ImportantDateInCalendarDto>> resolveImportantDatesForMonth(
            UUID roomId, int year, int month) {

        List<ImportantDate> allDates = importantDateRepository.findByRoomIdOrderByEventDateAsc(roomId);

        Map<LocalDate, List<ImportantDateInCalendarDto>> result = new HashMap<>();

        for (ImportantDate imp : allDates) {
            LocalDate resolvedDate = resolveDate(imp, year, month);
            if (resolvedDate != null) {
                result.computeIfAbsent(resolvedDate, k -> new ArrayList<>())
                        .add(toCalendarDto(imp));
            }
        }

        return result;
    }

    /**
     * Resuelve fechas importantes para un día específico.
     */
    private List<ImportantDateInCalendarDto> resolveImportantDatesForDay(
            UUID roomId, LocalDate date) {

        List<ImportantDate> allDates = importantDateRepository.findByRoomIdOrderByEventDateAsc(roomId);

        return allDates.stream()
                .filter(imp -> date.equals(resolveDate(imp, date.getYear(), date.getMonthValue())))
                .map(this::toCalendarDto)
                .toList();
    }

    /**
     * Resuelve si una ImportantDate cae en el mes dado.
     * - Recurrente: mismo month/day → ajusta al año actual
     * - No recurrente: solo si eventDate cae exactamente en el mes
     */
    private LocalDate resolveDate(ImportantDate imp, int year, int month) {
        if (imp.isRecurring()) {
            // ¿El mes/día del evento coincide con algún día de este mes?
            int eventMonth = imp.getEventDate().getMonthValue();
            int eventDay = imp.getEventDate().getDayOfMonth();

            if (eventMonth != month) return null;

            // Validar que el día existe en este mes (ej: 29 feb)
            if (eventDay > YearMonth.of(year, month).lengthOfMonth()) return null;

            return LocalDate.of(year, month, eventDay);
        } else {
            // Solo si cae exactamente en este mes/año
            if (imp.getEventDate().getYear() == year
                    && imp.getEventDate().getMonthValue() == month) {
                return imp.getEventDate();
            }
            return null;
        }
    }

    private ImportantDateInCalendarDto toCalendarDto(ImportantDate imp) {
        return new ImportantDateInCalendarDto(
                imp.getId(),
                imp.getType().getCode(),
                imp.getType().getName(),
                imp.getTitle(),
                imp.isRecurring()
        );
    }
}