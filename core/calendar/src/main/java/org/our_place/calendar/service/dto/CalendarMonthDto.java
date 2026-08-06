package org.our_place.calendar.service.dto;


import java.util.List;

public record CalendarMonthDto(
        int year,
        int month,
        List<CalendarDayDto> days
) {
}
