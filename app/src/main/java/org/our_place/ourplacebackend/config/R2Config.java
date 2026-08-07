package org.our_place.ourplacebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration; // <-- nuevo import
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class R2Config {

    @Value("${r2.account-id}")
    private String accountId;

    @Value("${r2.access-key}")
    private String accessKey;

    @Value("${r2.secret-key}")
    private String secretKey;

    private URI endpoint() {
        return URI.create("https://%s.r2.cloudflarestorage.com".formatted(accountId));
    }

    // Fuerza URLs path-style (host.com/bucket/key) en vez de virtual-hosted (bucket.host.com/key),
    // que es lo que R2 espera para que el preflight CORS resuelva correctamente.
    private S3Configuration pathStyleConfig() {
        return S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();
    }

    @Bean
    public AwsCredentialsProvider r2CredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }

    @Bean
    public S3Client r2Client(AwsCredentialsProvider r2CredentialsProvider) {
        return S3Client.builder()
                .endpointOverride(endpoint())
                .credentialsProvider(r2CredentialsProvider)
                .region(Region.of("auto"))
                .serviceConfiguration(pathStyleConfig()) // <-- nuevo
                .build();
    }

    @Bean
    public S3Presigner r2Presigner(AwsCredentialsProvider r2CredentialsProvider) {
        return S3Presigner.builder()
                .endpointOverride(endpoint())
                .credentialsProvider(r2CredentialsProvider)
                .region(Region.of("auto"))
                .serviceConfiguration(pathStyleConfig()) // <-- nuevo
                .build();
    }
}