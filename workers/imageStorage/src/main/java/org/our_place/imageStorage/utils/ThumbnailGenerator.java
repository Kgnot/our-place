package org.our_place.imageStorage.utils;


import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@Component
public class ThumbnailGenerator {

    private static final int THUMB_WIDTH = 400;
    private static final int THUMB_HEIGHT = 400;
    private static final float QUALITY = 0.85f;

    public byte[] generate(byte[] originalBytes, String mimeType)  {
        try (ByteArrayInputStream input = new ByteArrayInputStream(originalBytes);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Thumbnails.of(input)
                    .size(THUMB_WIDTH, THUMB_HEIGHT)
                    .outputQuality(QUALITY)
                    .outputFormat("jpeg")   // siempre JPEG para thumbnails
                    .toOutputStream(output);

            return output.toByteArray();

        } catch (Exception e) {
            throw new ImageProcessingException("Failed to generate thumbnail", e);
        }
    }
}