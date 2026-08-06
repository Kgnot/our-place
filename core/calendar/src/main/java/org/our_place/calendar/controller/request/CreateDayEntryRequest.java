package org.our_place.calendar.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateDayEntryRequest(
        @NotNull LocalDate entryDate,
        @Size(max = 10000) String content,
        @Size(max = 10) String moodEmoji
) {}