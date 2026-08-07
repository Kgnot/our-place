package org.our_place.map.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RecordLocationPingRequest(
        @NotBlank String locationWkt,
        @Min(0) @Max(100) Short batteryLevel
) {
}