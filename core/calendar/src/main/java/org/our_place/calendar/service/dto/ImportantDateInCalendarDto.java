package org.our_place.calendar.service.dto;

import java.util.UUID;

public record ImportantDateInCalendarDto(
        UUID id,
        String typeCode,
        String typeName,
        String title,
        boolean isRecurring
) {}