package org.our_place.affection.usecase.command;

import java.util.UUID;

public record CreateLoveNoteCommand(
        UUID roomId,
        UUID authorUserId,
        String typeCode,
        String content
) {}