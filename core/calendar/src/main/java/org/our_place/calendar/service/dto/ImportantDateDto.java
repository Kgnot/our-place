package org.our_place.calendar.service.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ImportantDateDto(
        UUID id,
        String typeCode,
        String typeName,
        String title,
        LocalDate eventDate,
        boolean isRecurring,
        List<MediaServiceDto> photos
) {}