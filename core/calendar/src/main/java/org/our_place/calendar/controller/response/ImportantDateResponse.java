package org.our_place.calendar.controller.response;


import org.our_place.calendar.service.dto.ImportantDateDetailDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ImportantDateResponse(
        UUID id,
        String typeCode,
        String typeName,
        String title,
        LocalDate eventDate,
        boolean isRecurring,
        short notifyDaysBefore,
        List<ImportantDatePhotoResponse> photos
) {
    public static ImportantDateResponse from(ImportantDateDetailDto dto) {
        return new ImportantDateResponse(
                dto.id(),
                dto.typeCode(),
                dto.typeName(),
                dto.title(),
                dto.eventDate(),
                dto.isRecurring(),
                dto.notifyDaysBefore(),
                dto.photos() != null ? dto.photos().stream()
                                       .map(ImportantDatePhotoResponse::from)
                                       .toList() : List.of()
        );
    }
}