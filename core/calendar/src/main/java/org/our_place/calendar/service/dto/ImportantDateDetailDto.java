package org.our_place.calendar.service.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ImportantDateDetailDto(
        UUID id,
        String typeCode,
        String typeName,
        String title,
        LocalDate eventDate,
        boolean isRecurring,
        short notifyDaysBefore,
        List<MediaServiceDto> photos
) {}