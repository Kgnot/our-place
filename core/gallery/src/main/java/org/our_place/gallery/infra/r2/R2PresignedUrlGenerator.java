package org.our_place.gallery.infra.r2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class R2PresignedUrlGenerator {

    private final S3Presigner s3Presigner;

    @Value("${r2.bucket}")
    private String bucket;

    public PresignedUpload generate(UUID roomId, UUID mediaId, String mimeType) {
        String r2Key = "rooms/%s/media/%s/%s".formatted(roomId, mediaId, UUID.randomUUID());

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(r2Key)
                .contentType(mimeType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();

        String uploadUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        return new PresignedUpload(uploadUrl, r2Key);
    }

    public record PresignedUpload(String uploadUrl, String r2Key) {}
}