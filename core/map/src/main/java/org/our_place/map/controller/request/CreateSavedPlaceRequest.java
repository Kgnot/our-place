package org.our_place.map.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateSavedPlaceRequest(
        @NotBlank String categoryCode,
        @NotBlank String name,
        String description,
        @NotBlank String locationWkt,
        LocalDate visitedAt
) {
}