package org.our_place.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.service.CalendarQueryService;
import org.our_place.calendar.service.dto.CalendarMonthDto;
import org.our_place.calendar.service.dto.DayEntryDetailDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarQueryService calendarQueryService;

    @GetMapping
    public ResponseEntity<CalendarMonthDto> getMonth(
            @PathVariable UUID roomId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(
                calendarQueryService.getMonth(roomId, year, month)
        );
    }

    @GetMapping("/{date}")
    public ResponseEntity<DayEntryDetailDto> getDayDetail(
            @PathVariable UUID roomId,
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(
                calendarQueryService.getDayDetail(roomId, date)
        );
    }
}
