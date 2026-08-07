package org.our_place.imageStorage.utils;


import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
@Component
public class ExifExtractor {

    public ExifData extract(byte[] imageBytes, String mimeType) {
        if (!mimeType.startsWith("image/")) {
            return ExifData.empty();
        }

        try (var input = new ByteArrayInputStream(imageBytes)) {
            Metadata metadata = ImageMetadataReader.readMetadata(input);

            OffsetDateTime takenAt = extractDateTaken(metadata);
            Double latitude = extractLatitude(metadata);
            Double longitude = extractLongitude(metadata);

            return new ExifData(takenAt, latitude, longitude);

        } catch (Exception e) {
            log.debug("Could not extract EXIF (not all images have it)", e);
            return ExifData.empty();
        }
    }

    private OffsetDateTime extractDateTaken(Metadata metadata) {
        var dir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (dir == null) return null;

        Date date = dir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        if (date == null) return null;

        return date.toInstant().atOffset(ZoneOffset.UTC);
    }

    private Double extractLatitude(Metadata metadata) {
        var dir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (dir == null) return null;
        return dir.getGeoLocation() != null ? dir.getGeoLocation().getLatitude() : null;
    }

    private Double extractLongitude(Metadata metadata) {
        var dir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (dir == null) return null;
        return dir.getGeoLocation() != null ? dir.getGeoLocation().getLongitude() : null;
    }
}