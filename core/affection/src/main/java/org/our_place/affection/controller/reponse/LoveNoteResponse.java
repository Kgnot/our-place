package org.our_place.affection.controller.reponse;

import org.our_place.affection.service.dto.LoveNoteDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LoveNoteResponse(
        UUID id,
        UUID authorUserId,
        String typeCode,
        String typeName,
        String content,
        OffsetDateTime createdAt
) {
    public static LoveNoteResponse from(LoveNoteDto dto) {
        return new LoveNoteResponse(
                dto.id(), dto.authorUserId(), dto.typeCode(), dto.typeName(), dto.content(), dto.createdAt()
        );
    }
}