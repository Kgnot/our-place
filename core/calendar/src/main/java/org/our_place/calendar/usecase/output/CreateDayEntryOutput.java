package org.our_place.calendar.usecase.output;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDayEntryOutput(UUID roomId, LocalDate entryDate) {}