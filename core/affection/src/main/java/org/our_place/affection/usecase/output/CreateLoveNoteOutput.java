package org.our_place.affection.usecase.output;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateLoveNoteOutput(
        UUID id,
        String typeCode,
        OffsetDateTime createdAt
) {}