package org.our_place.common.shared.r2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class R2PresignedUrlGenerator {

    private final S3Presigner s3Presigner;

    @Value("${r2.bucket}")
    private String bucket;

    /**
     * Genera una URL de subida firmada para un archivo a ser subido a R2.
     */
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

    /**
     * Genera una URL de lectura firmada para una key existente (thumbnail u original).
     * Devuelve null si key es null, para no romper media que todavía no tiene thumbnail generado.
     */
    public String generateGetUrl(String r2Key) {
        if (r2Key == null) return null;

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(r2Key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getRequest)
                .signatureDuration(Duration.ofHours(1))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public record PresignedUpload(String uploadUrl, String r2Key) {}
}