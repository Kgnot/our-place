package org.our_place.calendar.service.dto;

import org.our_place.gallery.application.service.dto.MediaSummaryDto;

import java.time.LocalDate;
import java.util.List;

/** Vista completa de un día */
public record DayEntryDetailDto(
        LocalDate date,
        String content,
        String moodEmoji,
        List<MediaSummaryDto> photos,  // todas las fotos del día
        List<ImportantDateInCalendarDto> importantDates
) {}
