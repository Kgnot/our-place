package org.our_place.imageStorage.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class R2StorageService {

    private final S3Client r2Client;

    @Value("${r2.bucket}")
    private String bucket;

    public byte[] download(String r2Key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(r2Key)
                .build();

        try (InputStream is = r2Client.getObject(request);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            is.transferTo(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to download from R2: " + r2Key, e);
        }
    }

    public void upload(String r2Key, byte[] data, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(r2Key)
                .contentType(contentType)
                .build();

        r2Client.putObject(request, RequestBody.fromBytes(data));
        log.debug("Uploaded to R2: {}", r2Key);
    }

    public void delete(String r2Key) {
        r2Client.deleteObject(builder -> builder
                .bucket(bucket)
                .key(r2Key)
                .build()
        );
        log.debug("Deleted from R2: {}", r2Key);
    }
}