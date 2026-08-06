package org.our_place.calendar.service.dto;

import java.time.LocalDate;
import java.util.List;

public record CalendarDayDto(
        LocalDate date,
        boolean hasEntry,
        String moodEmoji,
        List<MediaDetailDto> medias,
        int mediaCount
) {}
