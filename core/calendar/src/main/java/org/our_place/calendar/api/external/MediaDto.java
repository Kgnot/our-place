package org.our_place.calendar.api.external;

import java.util.UUID;

public record MediaDto(
        UUID id,
        String url
) {
}
