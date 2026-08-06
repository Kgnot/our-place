package org.our_place.calendar.usecase.command;

import java.time.LocalDate;
import java.util.UUID;

public record LinkMediaToDayEntryCommand(UUID roomId, LocalDate entryDate, UUID mediaId) {}