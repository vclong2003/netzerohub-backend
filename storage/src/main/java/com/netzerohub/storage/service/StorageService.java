package com.netzerohub.storage.service;

import com.netzerohub.storage.config.AwsProperties;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AwsProperties awsProperties;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String bucket = awsProperties.s3().bucket();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("s3://%s/%s", bucket, fileName);
    }

    public String generateSignedUrl(String s3Url) {
        if (s3Url == null || !s3Url.startsWith("s3://")) {
            throw new IllegalArgumentException("Invalid S3 URL format. Expected 's3://bucket/objectKey'");
        }

        String path = s3Url.substring(5);
        int slashIndex = path.indexOf('/');
        if (slashIndex == -1) {
            throw new IllegalArgumentException("Invalid S3 URL: Missing object key");
        }

        String bucket = path.substring(0, slashIndex);
        String objectKey = path.substring(slashIndex + 1);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(1))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public String getBucketPathFromSignedUrl(String signedUrl) {
        if (signedUrl == null || signedUrl.isBlank()) {
            throw new IllegalArgumentException("Signed URL cannot be empty");
        }

        try {
            // remove query string ? and fragment #
            String urlWithoutQuery = signedUrl;
            int queryIndex = urlWithoutQuery.indexOf('?');
            if (queryIndex != -1) {
                urlWithoutQuery = urlWithoutQuery.substring(0, queryIndex);
            }
            int fragmentIndex = urlWithoutQuery.indexOf('#');
            if (fragmentIndex != -1) {
                urlWithoutQuery = urlWithoutQuery.substring(0, fragmentIndex);
            }

            // remove host
            int schemeIndex = urlWithoutQuery.indexOf("://");
            String path;
            if (schemeIndex != -1) {
                int firstSlashAfterHost = urlWithoutQuery.indexOf('/', schemeIndex + 3);
                if (firstSlashAfterHost == -1) {
                    throw new IllegalArgumentException("Invalid signed URL: missing path");
                }
                path = urlWithoutQuery.substring(firstSlashAfterHost + 1);
            } else {
                path = urlWithoutQuery.startsWith("/") ? urlWithoutQuery.substring(1) : urlWithoutQuery;
            }

            // get bucket and object key
            int slashIndex = path.indexOf('/');
            if (slashIndex == -1 || slashIndex == path.length() - 1) {
                throw new IllegalArgumentException("Invalid path style URL: missing object key");
            }

            String bucket = path.substring(0, slashIndex);
            String objectKey = path.substring(slashIndex + 1);

            // decode objectKey if url encoded
            try {
                objectKey = URLDecoder.decode(objectKey, StandardCharsets.UTF_8);
            } catch (IllegalArgumentException ignored) {
            }

            return String.format("s3://%s/%s", bucket, objectKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse signed URL: " + e.getMessage(), e);
        }
    }
}