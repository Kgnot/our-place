package org.our_place.calendar.service.dto;

import org.our_place.gallery.application.service.dto.MediaSummaryDto;

import java.time.LocalDate;
import java.util.List;

public record CalendarDayDto(
        LocalDate date,
        boolean hasEntry,          // hay diario/mood?
        String moodEmoji,
        boolean hasPhotos,         // hay fotos?
        int photoCount,
        List<MediaSummaryDto> previewPhotos,   // 4 thumbnails
        List<ImportantDateInCalendarDto> importantDates
) {}
