package org.our_place.calendar.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateImportantDateRequest(
        @NotBlank String typeCode,
        @NotBlank String title,
        @NotNull LocalDate eventDate,
        boolean isRecurring,
        short notifyDaysBefore
) {}