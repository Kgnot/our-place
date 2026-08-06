package org.our_place.calendar.usecase.command;

import java.time.LocalDate;
import java.util.UUID;

public record CreateImportantDateCommand(
        UUID roomId,
        String typeCode,
        String title,
        LocalDate eventDate,
        boolean isRecurring,
        short notifyDaysBefore,
        UUID createdByUserId
) {}