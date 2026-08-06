package org.our_place.calendar.usecase.command;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDayEntryCommand(
        UUID roomId,
        LocalDate entryDate,
        UUID createdByUserId,
        String content,
        String moodEmoji
) {}