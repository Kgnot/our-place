package org.our_place.imageStorage.utils;

import java.time.OffsetDateTime;

public record ExifData(
        OffsetDateTime takenAt,
        Double latitude,
        Double longitude
) {
    public static ExifData empty() {
        return new ExifData(null, null, null);
    }

    public boolean hasLocation() {
        return latitude != null && longitude != null;
    }
}