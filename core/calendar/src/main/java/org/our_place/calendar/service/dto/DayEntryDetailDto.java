package org.our_place.calendar.service.dto;

import java.time.LocalDate;
import java.util.List;

/** Vista completa de un día */
public record DayEntryDetailDto(
        LocalDate date,
        String content,
        String moodEmoji,
        List<MediaDetailDto> mediaIds
) {


}