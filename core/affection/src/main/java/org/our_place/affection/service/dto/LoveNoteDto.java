package org.our_place.affection.service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LoveNoteDto(
        UUID id,
        UUID authorUserId,
        String typeCode,
        String typeName,
        String content,
        OffsetDateTime createdAt
) {}