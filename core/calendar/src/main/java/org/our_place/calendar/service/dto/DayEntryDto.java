package org.our_place.calendar.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public record DayEntryDto(UUID roomId, LocalDate entryDate, UUID createdByUserId, String content, String moodEmoji) {}