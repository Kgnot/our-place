package org.our_place.ourplacebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
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
                .build();
    }

    @Bean
    public S3Presigner r2Presigner(AwsCredentialsProvider r2CredentialsProvider) {
        return S3Presigner.builder()
                .endpointOverride(endpoint())
                .credentialsProvider(r2CredentialsProvider)
                .region(Region.of("auto"))
                .build();
    }
}